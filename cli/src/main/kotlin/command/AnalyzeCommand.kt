package command

import cli.utils.EnvironmentCreator
import cli.utils.ProgressTracker
import lexer.Lexer
import linter.Linter
import linter.LinterResult
import parser.Parser
import rules.LinterRules

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
        tokens: Lexer,
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

            processedChars = ProgressTracker.updateProgress(tokens, processedChars, totalChars)
        }

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
