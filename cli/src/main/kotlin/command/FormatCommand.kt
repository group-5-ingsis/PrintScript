package command

import cli.FileReader
import cli.FileWriter
import formatter.Formatter
import lexer.Lexer
import parser.Parser

class FormatCommand(private val file: String, private val version: String, private val rulesFile: String) : Command {
    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)
        val formattingRules = FileReader.getFormattingRules(rulesFile, version)

        return try {
            val tokens = Lexer(fileContent)

            val ast = Parser(tokens)

            val formatter = Formatter(ast)

            val result = formatter.format(formattingRules)

            FileWriter.writeToFile(file, version, result)

            "File formatted successfully"
        } catch (e: Exception) {
            "Formatting Error: ${e.message}"
        }
    }
}
