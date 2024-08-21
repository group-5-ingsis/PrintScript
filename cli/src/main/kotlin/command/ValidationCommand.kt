package command

import cli.FileReader
import lexer.Lexer
import parser.Parser

class ValidationCommand(private val fileLocation: String) : Command {
  override fun execute(): String {
    val fileContent = FileReader.getFileContents(fileLocation)
    try {
      val tokens = Lexer.lex(fileContent, listOf())
      Parser().run(tokens)
      return "File Validated"
    } catch (e: Exception) {
      return e.toString()
    }
  }
}
