package command.validate

import command.Command
import lexer.Lexer
import parser.Parser
import utils.FileReader
import utils.ProgressTracker

class ValidationCommand(private val file: String, private val version: String) : Command {

    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)
        val totalChars = fileContent.length
        var lastProcessedChars = 0

        return try {
            val tokens = Lexer(fileContent)
            val astNodes = Parser(tokens)

            while (astNodes.hasNext()) {
                astNodes.next()
                val processedChars = tokens.getProcessedCharacters()

                ProgressTracker.updateProgress(processedChars, totalChars)

                lastProcessedChars = processedChars
            }

            if (fileContent.isNotEmpty()) {
                ProgressTracker.updateProgress(totalChars, totalChars)
            }

            "File Validated! (No Errors found)"
        } catch (e: Exception) {
            "Validation error: ${e.message}"
        }
    }
}
