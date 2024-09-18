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
import kotlin.math.roundToInt

class ExecuteCommand(
    private val file: String,
    private val version: String
) : Command {

    private var progress: Int = 0

    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)
        val totalCharacters = fileContent.length

        var processedCharacters = 0
        var lastProcessedPosition = Position(0, 0)
        val outputBuilder = StringBuilder()

        return try {
            val tokens = Lexer(fileContent, version)
            val asts = Parser(tokens, version)

            var currentEnvironment = createEnvironmentFromMap(System.getenv())

            while (asts.hasNext()) {
                val statement = asts.next()
                val result = Interpreter.interpret(statement, version, currentEnvironment)
                outputBuilder.append(result.first.toString())

                val endPosition = statement.position

                currentEnvironment = result.second
                processedCharacters += ProgressTracker.calculateProcessedCharacters(fileContent, lastProcessedPosition, endPosition)
                lastProcessedPosition = endPosition

                progress = (processedCharacters.toDouble() / totalCharacters * 100).roundToInt()
                reportProgress(progress)
            }

            if (processedCharacters < totalCharacters) {
                processedCharacters = totalCharacters
                progress = 100
                reportProgress(progress)
            }

            "${outputBuilder}\nFinished executing $file"
        } catch (e: Exception) {
            "Execution Error: ${e.message}"
        }
    }

    private fun reportProgress(progress: Int) {
        println("Progress: $progress%")
    }

    override fun getProgress(): Int {
        return progress
    }

    private fun createEnvironmentFromMap(envVarsMap: Map<String, String>): Environment {
        var env = Environment()

        for ((key, value) in envVarsMap) {
            val variable = StatementType.Variable(
                designation = "const",
                identifier = key,
                initializer = Expression.Literal(value, Position(0, 0)),
                dataType = "string",
                position = Position(0, 0)
            )

            env = env.define(variable)
        }

        return env
    }
}
