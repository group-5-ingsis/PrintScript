package command

import cli.FileReader
import cli.FileWriter
import cli.ProgressTracker
import formatter.Formatter
import lexer.Lexer
import parser.Parser
import position.Position

class FormatCommand(
    private val file: String,
    private val version: String,
    private val rulesFile: String
) : Command {
    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)
        val formattingRules = FileReader.getFormattingRules(rulesFile, version)
        val totalCharacters = fileContent.length - 1

        var processedCharacters = 0
        var lastProcessedPosition = Position(0, 0)
        val outputBuilder = StringBuilder()

        try {
            val tokens = Lexer(fileContent)
            val astNodes = Parser(tokens)

            while (astNodes.hasNext()) {
                val statement = astNodes.next()
                val formattedNode = Formatter.format(statement, formattingRules, version)
                outputBuilder.append(formattedNode)

                val endPosition = statement.position

                processedCharacters += ProgressTracker.calculateProcessedCharacters(fileContent, lastProcessedPosition, endPosition)
                lastProcessedPosition = endPosition

                ProgressTracker.updateProgress(processedCharacters, totalCharacters)
            }

            if (processedCharacters < totalCharacters) {
                ProgressTracker.updateProgress(totalCharacters, totalCharacters)
            }

            val formattedResult = outputBuilder.toString()
            FileWriter.writeToFile(file, version, formattedResult)

            return "File formatted successfully"
        } catch (e: Exception) {
            return "Formatting Error: ${e.message}"
        }
    }

    override fun getProgress(): Int {
        return ProgressTracker.getProgress()
    }
}
