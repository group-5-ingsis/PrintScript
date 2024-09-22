package command

import Environment
import cli.utils.EnvironmentCreator
import cli.utils.FileReader
import cli.utils.ProgressTracker
import interpreter.Interpreter
import lexer.Lexer
import nodes.Expression
import nodes.StatementType
import parser.Parser

class ExecuteCommand(
    private val filePath: String,
    private val version: String
) : Command {

    override fun execute(): String {
        val fileContent = FileReader.getFileContents(filePath, version)
        return try {
            val lexer = Lexer(fileContent, version)
            val astNodes = Parser(lexer, version)
            val result = executeFile(astNodes, lexer, fileContent)
            "$result\nFinished executing $filePath"
        } catch (e: Exception) {
            "Execution Error: ${e.message}"
        }
    }

    private fun executeFile(
        astNodes: Parser,
        lexer: Lexer,
        fileContent: String
    ): String {
        val totalChars = fileContent.length
        var processedChars = 0
        val output = StringBuilder()

        var currentEnv = EnvironmentCreator.create(System.getenv())

        while (astNodes.hasNext()) {
            currentEnv = astNodes.setEnv(currentEnv)
            val statement = astNodes.next()
            val (outputFragment, updatedEnv) = processStatement(version, statement, astNodes, currentEnv)
            output.append(outputFragment)
            currentEnv = updatedEnv

            processedChars = ProgressTracker.updateProgress(lexer, processedChars, totalChars)
        }

        return output.toString()
    }

    private fun processStatement(
        version: String,
        statement: StatementType,
        astNodes: Parser,
        currentEnv: Environment
    ): Pair<StringBuilder, Environment> {
        return when (statement) {
            is StatementType.Variable -> {
                val initializer = statement.initializer
                if (initializer is Expression.ReadInput) {
                    handleReadInput(statement, initializer, astNodes, version, currentEnv)
                } else {
                    Interpreter.interpret(statement, version, currentEnv, null)
                }
            }
            else -> Interpreter.interpret(statement, version, currentEnv, null)
        }
    }

    private fun handleReadInput(
        statement: StatementType.Variable,
        initializer: Expression.ReadInput,
        astNodes: Parser,
        version: String,
        currentEnv: Environment
    ): Pair<StringBuilder, Environment> {
        val value = initializer.value
        return if (value.expression is Expression.Literal) {
            print((value.expression as Expression.Literal).value)
            val input = readln()
            astNodes.setInput(input)
            Interpreter.interpret(statement, version, currentEnv, input)
        } else {
            Interpreter.interpret(statement, version, currentEnv, null)
        }
    }
}
