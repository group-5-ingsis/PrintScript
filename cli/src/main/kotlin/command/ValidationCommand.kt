package command

import cli.FileReader
import interpreter.Interpreter
import lexer.Lexer
import parser.Parser

class ValidationCommand(private val file: String, private val version: String) : Command {
    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)

        if (fileContent.startsWith("Error") || fileContent.startsWith("File not found")) {
            return fileContent
        }

        return try {
            val statements = fileContent.split("(?<=;)".toRegex()).filter { it.isNotBlank() }

            val parser = Parser()

            for (statement in statements) {
                val trimmedStatement = statement.trim()

                val tokens = Lexer.lex(trimmedStatement)

                val ast = parser.run(tokens)

                Interpreter.interpret(ast)
            }

            "File Validated"
        } catch (e: Exception) {
            "Validation error: ${e.message}"
        }
    }
}
