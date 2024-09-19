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
        val totalChars = fileContent.length

        var lastPosition = Position(0, 0)
        val output = StringBuilder()

        return try {
            val tokens = Lexer(fileContent, version)
            val astNodes = Parser(tokens, version)

            var currentEnv = createEnvironment(System.getenv())

            while (astNodes.hasNext()) {
                astNodes.setEnv(currentEnv)
                val statement = astNodes.next()
                val (outputFragment, updatedEnv) = processStatement(version, statement, astNodes, currentEnv)
                output.append(outputFragment)

                val endPosition = statement.position
                currentEnv = updatedEnv

                val processedChars = ProgressTracker.calculateProcessedCharacters(fileContent, lastPosition, endPosition)
                ProgressTracker.updateProgress(processedChars, totalChars)
                progressPercentage = ProgressTracker.getProgress()

                lastPosition = endPosition
            }

            if (fileContent.isNotEmpty()) {
                ProgressTracker.updateProgress(totalChars, totalChars)
                progressPercentage = ProgressTracker.getProgress()
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

    private fun processStatement(
        version: String,
        statement: StatementType,
        astNodes: Parser,
        currentEnv: Environment
    ): Pair<StringBuilder, Environment> {
        var result = Pair(StringBuilder(), currentEnv)

        if (statement is StatementType.Variable) {
            val initializer = statement.initializer
            if (initializer is Expression.ReadInput) {
                val value = initializer.value
                if (value.expression is Expression.Literal) {
                    print((value.expression as Expression.Literal).value)
                    val input = readln()
                    astNodes.setInput(input)
                    result = Interpreter.interpret(statement, version, currentEnv, input)
                } else {
                    result = Interpreter.interpret(statement, version, currentEnv, null)
                }
            } else {
                result = Interpreter.interpret(statement, version, currentEnv, null)
            }
        } else {
            result = Interpreter.interpret(statement, version, currentEnv, null)
        }

        return result
    }
}
