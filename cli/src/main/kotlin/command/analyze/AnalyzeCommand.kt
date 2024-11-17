package command.analyze

import command.Command
import environment.EnvironmentCreator
import lexer.Lexer
import linter.Linter
import linter.LinterResult
import parser.Parser
import rules.LinterRules
import utils.ProgressTracker

class AnalyzeCommand(
  private val fileContent: String,
  private val version: String,
  private val rules: LinterRules
) : Command {

  override fun execute(): String {
    return try {
      val tokens = Lexer(fileContent, version)
      val astNodes = Parser(tokens, version)
      val result = analyzeFile(astNodes, tokens, rules)
      return result
    } catch (e: Exception) {
      "Error during analysis: ${e.message}"
    }
  }

  private fun analyzeFile(
    astNodes: Parser,
    lexer: Lexer,
    linterRules: LinterRules
  ): String {
    val totalChars = fileContent.length
    var processedChars = 0

    var errorList: List<LinterResult> = emptyList()

    var currentEnv = EnvironmentCreator.create(System.getenv())

    while (astNodes.hasNext()) {
      currentEnv = astNodes.setEnv(currentEnv)
      val statement = astNodes.next()
      val lintResult = Linter.lint(statement, linterRules, version)

      if (!lintResult.isValid()) {
        errorList = errorList + lintResult
      }

      currentEnv = astNodes.getEnv()

      processedChars = ProgressTracker.updateProgress(lexer, processedChars, totalChars)
    }

    processedChars = ProgressTracker.updateProgress(lexer, totalChars, totalChars)

    return lintingResult(errorList)
  }

  private fun lintingResult(errorList: List<LinterResult>): String {
    return if (errorList.isEmpty()) {
      "No problems found"
    } else {
      errorList.mapIndexed { index, error ->
        "Error ${index + 1}:\n${error.message}"
      }.joinToString(separator = "\n\n")
    }
  }
}
