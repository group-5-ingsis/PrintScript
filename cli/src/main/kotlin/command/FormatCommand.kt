package command

import cli.FileReader
import cli.FileWriter
import formatter.Formatter
import lexer.Lexer
import parser.Parser
import kotlin.math.roundToInt

class FormatCommand(
    private val file: String,
    private val version: String,
    private val rulesFile: String
) : Command {
    private var progress: Int = 0

    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)
        val formattingRules = FileReader.getFormattingRules(rulesFile, version)
        val totalCharacters = fileContent.length

        var processedCharacters = 0
        val outputBuilder = StringBuilder()

        try {
            val tokens = Lexer(fileContent)
            val astNodes = Parser(tokens)

            while (astNodes.hasNext()) {
                val statement = astNodes.next()
                val formattedNode = Formatter.format(statement, formattingRules, version)
                outputBuilder.append(formattedNode)

                processedCharacters = calculateProcessedCharacters(fileContent, processedCharacters, statement)
                progress = (processedCharacters.toDouble() / totalCharacters * 100).roundToInt()
                reportProgress(progress)
            }

            val formattedResult = outputBuilder.toString()
            FileWriter.writeToFile(file, version, formattedResult)

            return "File formatted successfully"
        } catch (e: Exception) {
            return "Formatting Error: ${e.message}"
        }
    }

    private fun calculateProcessedCharacters(fileContent: String, processedCharacters: Int, statement: Any): Int {
        return processedCharacters
    }

    override fun getProgress(): Int {
        return progress
    }

    private fun reportProgress(progress: Int) {
        println("Formatting Progress: $progress%")
    }
}
