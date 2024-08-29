package command

import cli.FileReader
import formatter.FileWriter
import formatter.Formatter
import interpreter.Interpreter
import lexer.Lexer
import parser.Parser

class FormatCommand(private val file: String, private val version: String, private val rulesFile: String) : Command {
    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)
        val formattingRules = FileReader.getFormattingRules(rulesFile, version)

        return try {
            val tokens = Lexer.lex(fileContent, listOf())

            val ast = Parser().run(tokens)

            Interpreter.interpret(ast)

            val formattedContent = StringBuilder()

            for (child in ast.getChildren()) {
                val formattedLine = Formatter.format(child, formattingRules)
                formattedContent.append(formattedLine)
            }

            val formattedFile = formattedContent.toString().trimEnd()
            FileWriter.writeToFile(file, formattedFile)

            "File formatted successfully"
        } catch (e: Exception) {
            "Validation error: ${e.message}"
        }
    }
}
