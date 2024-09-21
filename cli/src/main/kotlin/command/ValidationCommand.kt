package command

import cli.utils.FileReader
import cli.utils.ProgressTracker
import lexer.Lexer
import parser.Parser

class ValidationCommand(private val file: String, private val version: String) : Command {
    private var progress: Int = 0

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

    override fun getProgress(): Int {
        return progress
    }
}
