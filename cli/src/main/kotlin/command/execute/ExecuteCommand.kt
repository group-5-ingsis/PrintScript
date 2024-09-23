package command.execute

import command.Command
import environment.Environment
import environment.EnvironmentCreator
import interpreter.Interpreter
import lexer.Lexer
import nodes.Expression
import nodes.StatementType
import parser.Parser
import utils.ProgressTracker
import visitor.PrintScriptInputProvider

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
            val (output, updatedEnv) = processStatement(version, statement, currentEnv)
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
        statement: StatementType,
        currentEnv: Environment
    ): Pair<StringBuilder, Environment> {
        return when (statement) {
            is StatementType.Variable -> {
                val initializer = statement.initializer
                if (initializer is Expression.ReadInput) {
                    handleReadInput(statement, initializer, version, currentEnv)
                } else {
                    Interpreter.interpret(statement, version, currentEnv)
                }
            }
            else -> Interpreter.interpret(statement, version, currentEnv)
        }
    }

    private fun handleReadInput(
        statement: StatementType.Variable,
        initializer: Expression.ReadInput,
        version: String,
        currentEnv: Environment
    ): Pair<StringBuilder, Environment> {
        val value = initializer.value
        return if (value.expression is Expression.Literal) {
            print((value.expression as Expression.Literal).value)
            val input = PrintScriptInputProvider()
            Interpreter.interpret(statement, version, currentEnv, input)
        } else {
            Interpreter.interpret(statement, version, currentEnv)
        }
    }
}
