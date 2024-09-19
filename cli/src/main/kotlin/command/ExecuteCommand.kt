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
    private val file: String,
    private val version: String
) : Command {

    private var progress: Int = 0

    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)
        val totalCharacters = fileContent.length

        var lastProcessedPosition = Position(0, 0)
        val outputBuilder = StringBuilder()

        return try {
            val tokens = Lexer(fileContent, version)
            val asts = Parser(tokens, version)

            var currentEnvironment = createEnvironmentFromMap(System.getenv())

            while (asts.hasNext()) {
                val statement = asts.next()
                val result = getStringBuilderEnvironmentPair(version, statement, asts, currentEnvironment)
                outputBuilder.append(result.first.toString())

                val endPosition = statement.position

                currentEnvironment = result.second
                val processedCharacters = ProgressTracker.calculateProcessedCharacters(fileContent, lastProcessedPosition, endPosition)
                ProgressTracker.updateProgress(processedCharacters, totalCharacters)
                progress = ProgressTracker.getProgress()

                lastProcessedPosition = endPosition
            }

            if (fileContent.isNotEmpty()) {
                ProgressTracker.updateProgress(totalCharacters, totalCharacters)
                progress = ProgressTracker.getProgress()
            }

            "${outputBuilder}\nFinished executing $file"
        } catch (e: Exception) {
            "Execution Error: ${e.message}"
        }
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

    private fun getStringBuilderEnvironmentPair(
        version: String,
        statement: StatementType,
        asts: Parser,
        currentEnvironment: Environment
    ): Pair<StringBuilder, Environment> {
        var result: Pair<StringBuilder, Environment> = Pair(StringBuilder(), currentEnvironment)

        if (statement is StatementType.Variable) {
            val initializer = statement.initializer
            if (initializer is Expression.ReadInput) {
                val value = initializer.value
                if (value is Expression.Grouping) {
                    val expression = value.expression
                    if (expression is Expression.Literal) {
                        val message = expression.value
                        print(message)
                        val input: String = readln()
                        asts.setInput(input)
                        result = Interpreter.interpret(statement, version, currentEnvironment, input)
                    }
                } else {
                    result = Interpreter.interpret(statement, version, currentEnvironment, null)
                }
            } else {
                result = Interpreter.interpret(statement, version, currentEnvironment, null)
            }
        } else {
            result = Interpreter.interpret(statement, version, currentEnvironment, null)
        }

        return result
    }
}
