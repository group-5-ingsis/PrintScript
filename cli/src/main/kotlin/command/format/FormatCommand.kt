package command.format

import command.Command
import formatter.Formatter
import lexer.Lexer
import parser.Parser
import utils.FileReader
import utils.FileWriter
import utils.ProgressTracker

class FormatCommand(
    private val file: String,
    private val version: String,
    private val rulesFile: String
) : Command {

    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)
        val formattingRules = FileReader.getFormattingRules(rulesFile, version)
        val outputBuilder = StringBuilder()

        try {
            val tokens = Lexer(fileContent)
            val astNodes = Parser(tokens)

            val totalChars = fileContent.length
            var lastProcessedChars = 0

            while (astNodes.hasNext()) {
                val statement = astNodes.next()
                val formattedNode = Formatter.format(statement, formattingRules, version)
                outputBuilder.append(formattedNode)

                val processedChars = tokens.getProcessedCharacters()

                ProgressTracker.updateProgress(processedChars, totalChars)

                lastProcessedChars = processedChars
            }

            if (fileContent.isNotEmpty()) {
                ProgressTracker.updateProgress(totalChars, totalChars)
            }

            val formattedResult = outputBuilder.toString()
            FileWriter.writeToFile(file, version, formattedResult)

            return "File formatted successfully"
        } catch (e: Exception) {
            return "Formatting Error: ${e.message}"
        }
    }
}
