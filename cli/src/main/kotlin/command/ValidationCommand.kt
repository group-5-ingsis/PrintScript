package command

import cli.FileReader
import cli.ProgressTracker
import lexer.Lexer
import parser.Parser
import position.Position

class ValidationCommand(private val file: String, private val version: String) : Command {
    private var progress: Int = 0

    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)
        val totalCharacters = fileContent.length

        var lastProcessedPosition = Position(0, 0)

        return try {
            val tokens = Lexer(fileContent)
            val astNodes = Parser(tokens)

            while (astNodes.hasNext()) {
                val statement = astNodes.next()
                val endPosition = statement.position

                val processedCharacters = ProgressTracker.calculateProcessedCharacters(fileContent, lastProcessedPosition, endPosition)
                ProgressTracker.updateProgress(processedCharacters, totalCharacters)
                progress = ProgressTracker.getProgress()

                lastProcessedPosition = endPosition
            }

            if (totalCharacters > 0) {
                ProgressTracker.updateProgress(totalCharacters, totalCharacters)
                progress = ProgressTracker.getProgress()
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
