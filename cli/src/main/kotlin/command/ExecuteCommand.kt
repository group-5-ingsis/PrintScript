package command

import cli.FileReader
import interpreter.Interpreter
import lexer.Lexer
import parser.Parser

class ExecuteCommand(private val file: String, private val version: String) : Command {
    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)

        if (fileContent.startsWith("Error") || fileContent.startsWith("File not found")) {
            return fileContent
        }

        return try {
            val tokens = Lexer(fileContent, version)

            val ast = Parser(tokens, version)

            val output = Interpreter(ast)

            "$output\nFinished executing $file"
        } catch (e: Exception) {
            "Execution Error: ${e.message}"
        }
    }
}
