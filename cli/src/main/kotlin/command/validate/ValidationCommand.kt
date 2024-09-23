package command.validate

import command.Command
import lexer.Lexer
import parser.Parser
import utils.EnvironmentCreator
import utils.FileReader
import utils.ProgressTracker

class ValidationCommand(private val file: String, private val version: String) : Command {

    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)
        val totalChars = fileContent.length

        return try {
            val tokens = Lexer(fileContent)
            val astNodes = Parser(tokens)

            validateNodes(astNodes, tokens, totalChars)

            "File Validated! (No Errors found)"
        } catch (e: Exception) {
            "Validation error: ${e.message}"
        }
    }

    private fun validateNodes(astNodes: Parser, lexer: Lexer, totalChars: Int) {
        var processedChars = 0

        var currentEnv = EnvironmentCreator.create(System.getenv())

        while (astNodes.hasNext()) {
            currentEnv = astNodes.setEnv(currentEnv)
            astNodes.next()
            currentEnv = astNodes.getEnv()
            processedChars = ProgressTracker.updateProgress(lexer, processedChars, totalChars)
        }

        processedChars = ProgressTracker.updateProgress(lexer, totalChars, totalChars)
    }
}
