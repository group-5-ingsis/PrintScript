package command.execute

import command.Command
import environment.Environment
import environment.EnvironmentCreator
import interpreter.Interpreter
import lexer.Lexer
import nodes.Expression
import nodes.Statement
import parser.Parser
import utils.ProgressTracker

class ExecuteCommand(
    private val fileContent: String,
    private val version: String
) : Command {

    override fun execute(): String {
        return try {
            val lexer = Lexer(fileContent, version)
            val astNodes = Parser(lexer, version)
            return executeFile(astNodes, lexer, fileContent)
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
        var outputEmitter = StringBuilder()

        var currentEnv = EnvironmentCreator.create(System.getenv())

        while (astNodes.hasNext()) {
            currentEnv = astNodes.setEnv(currentEnv)
            val statement = astNodes.next()
            val (output, updatedEnv) = processStatement(version, statement, astNodes, currentEnv)
            outputEmitter = outputEmitter.append(output)
            currentEnv = updatedEnv

            processedChars = ProgressTracker.updateProgress(lexer, processedChars, totalChars)
        }

        processedChars = ProgressTracker.updateProgress(lexer, totalChars, totalChars)

        val printResult = outputEmitter.toString()
        return "$printResult\nFile Executed!"
    }

    private fun processStatement(
        version: String,
        statement: Statement,
        astNodes: Parser,
        currentEnv: Environment
    ): Pair<StringBuilder, Environment> {
        return when (statement) {
            is Statement.Variable -> {
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
        statement: Statement.Variable,
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
