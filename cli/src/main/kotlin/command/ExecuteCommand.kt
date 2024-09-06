package command

import cli.FileReader
import interpreter.Interpreter
import lexer.TokenIterator
import parser.Parser

class ExecuteCommand(private val file: String, private val version: String) : Command {
    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)

        if (fileContent.startsWith("Error") || fileContent.startsWith("File not found")) {
            return fileContent
        }

        return try {
            val tokens = TokenIterator.lex(fileContent)

            val ast = Parser().run(tokens)

            val output = Interpreter.interpret(ast)

            "$output\nFinished executing $file"
        } catch (e: Exception) {
            "Validation error: ${e.message}"
        }
    }
}
