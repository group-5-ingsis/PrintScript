package command

import Environment
import cli.FileReader
import cli.ProgressTracker
import interpreter.Interpreter
import lexer.Lexer
import nodes.Expression
import nodes.StatementType
import parser.Parser
import position.Position

class ExecuteCommand(
    private val filePath: String,
    private val version: String
) : Command {

    private var progressPercentage: Int = 0

    override fun execute(): String {
        val fileContent = FileReader.getFileContents(filePath, version)

        return try {
            val lexer = Lexer(fileContent, version)
            val astNodes = Parser(lexer, version)
            var currentEnv = createEnvironment(System.getenv())

            val totalChars = fileContent.length
            var lastProcessedChars = 0

            var output = StringBuilder()

            while (astNodes.hasNext()) {
                // Execute de code
                astNodes.setEnv(currentEnv)
                val statement = astNodes.next()
                val (outputFragment, updatedEnv) = Interpreter.interpret(statement, version, currentEnv, output)

                // re-assign results of execution
                output = outputFragment
                currentEnv = updatedEnv

                // update progress bar
                val processedChars = lexer.getProcessedCharacters()
                ProgressTracker.updateProgress(processedChars, totalChars)
                lastProcessedChars = processedChars
            }

            if (fileContent.isNotEmpty()) {
                ProgressTracker.updateProgress(totalChars, totalChars)
            }

            "$output\nFinished executing $filePath"
        } catch (e: Exception) {
            "Execution Error: ${e.message}"
        }
    }

    override fun getProgress(): Int {
        return progressPercentage
    }

    private fun createEnvironment(envVars: Map<String, String>): Environment {
        var environment = Environment()

        for ((key, value) in envVars) {
            val variable = StatementType.Variable(
                designation = "const",
                identifier = key,
                initializer = Expression.Literal(value, Position(0, 0)),
                dataType = getDataType(value),
                position = Position(0, 0)
            )

            environment = environment.define(variable)
        }

        return environment
    }

    private fun getDataType(value: String): String {
        return when {
            value.equals("true", ignoreCase = true) || value.equals("false", ignoreCase = true) -> "boolean"
            value.toIntOrNull() != null -> "number"
            else -> "string"
        }
    }


}
