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
      val tokens = Lexer.lex(fileContent, listOf())

      val ast = Parser().run(tokens)

      Interpreter.interpret(ast)

      " Finished executing $file"
    } catch (e: Exception) {
      "Validation error: ${e.message}"
    }
  }
}
