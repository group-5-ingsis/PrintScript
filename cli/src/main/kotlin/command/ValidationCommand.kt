 
package command

import cli.FileReader
import cli.ProgressTracker
import lexer.Lexer
import parser.Parser
import position.Position
import kotlin.math.roundToInt

class ValidationCommand(private val file: String, private val version: String) : Command {
    private var progress: Int = 0

    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)

        val totalCharacters = fileContent.length
        var processedCharacters = 0
        var lastProcessedPosition = Position(0, 0)

        return try {
            val tokens = Lexer(fileContent)
            val astNodes = Parser(tokens)

            while (astNodes.hasNext()) {
                val statement = astNodes.next()
                val endPosition = statement.position

                processedCharacters += ProgressTracker.calculateProcessedCharacters(fileContent, lastProcessedPosition, endPosition)
                lastProcessedPosition = endPosition

                progress = (processedCharacters.toDouble() / totalCharacters * 100).roundToInt()
                reportProgress(progress)
            }

            if (processedCharacters < totalCharacters) {
                processedCharacters = totalCharacters
                progress = 100
                reportProgress(progress)
            }

            "File Validated! (No Errors found)"
        } catch (e: Exception) {
            "Validation error: ${e.message}"
        }
    }

    private fun reportProgress(progress: Int) {
        println("Progress: $progress%")
    }

    override fun getProgress(): Int {
        return progress
    }
}
