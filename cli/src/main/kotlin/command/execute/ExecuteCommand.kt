package command.execute

import command.Command
import environment.EnvironmentCreator
import interpreter.Interpreter
import lexer.Lexer
import parser.Parser
import utils.PrintScriptInputProvider
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
            val (outPut, updatedEnv) = Interpreter.interpret(statement, version, currentEnv, PrintScriptInputProvider())
            outputEmitter = outPut
            currentEnv = updatedEnv

            processedChars = ProgressTracker.updateProgress(lexer, processedChars, totalChars)
        }

        processedChars = ProgressTracker.updateProgress(lexer, totalChars, totalChars)

        val printResult = outputEmitter.toString()
        return "$printResult\nFile Executed!"
    }
}
