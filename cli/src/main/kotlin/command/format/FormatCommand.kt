package command.format

import command.Command
import environment.EnvironmentCreator
import formatter.Formatter
import lexer.Lexer
import parser.Parser
import rules.FormattingRules
import utils.FileReader
import utils.FileWriter
import utils.ProgressTracker

class FormatCommand(
  private val file: String,
  private val version: String,
  private val formattingRules: FormattingRules
) : Command {

  override fun execute(): String {
    return try {
      val fileContent = FileReader.getFileContents(file, version)
      val tokens = Lexer.fromString(fileContent)
      val astNodes = Parser(tokens)

      val formattedResult = formatFile(astNodes, tokens, formattingRules, fileContent)
      val formattedFile = FileWriter.writeToFile(file, version, formattedResult)

      return "${formattedFile.name} formatted successfully"
    } catch (e: Exception) {
      "Formatting Error: ${e.message}"
    }
  }

  private fun formatFile(
    astNodes: Parser,
    lexer: Lexer,
    formattingRules: FormattingRules,
    fileContent: String
  ): String {
    val totalChars = fileContent.length
    var processedChars = 0
    var outputEmitter = StringBuilder()

    var currentEnv = EnvironmentCreator.create(System.getenv())

    while (astNodes.hasNext()) {
      currentEnv = astNodes.setEnv(currentEnv)
      val statement = astNodes.next()
      val formattedStatement = Formatter.format(statement, formattingRules, version)

      outputEmitter = outputEmitter.append(formattedStatement)

      currentEnv = astNodes.getEnv()

      processedChars = ProgressTracker.updateProgress(lexer, processedChars, totalChars)
    }

    processedChars = ProgressTracker.updateProgress(lexer, totalChars, totalChars)

    return outputEmitter.toString().trim()
  }
}
