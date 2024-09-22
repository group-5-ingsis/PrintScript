package command

import cli.utils.FileReader
import cli.utils.ProgressTracker
import lexer.Lexer
import linter.Linter
import linter.LinterResult
import parser.Parser

class AnalyzeCommand(private val file: String, private val version: String, private val rulesFile: String) : Command {

    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)
        val linterRules = FileReader.getLinterRules(rulesFile, version)

        val errorList = mutableListOf<LinterResult>()

        try {
            val tokens = Lexer(fileContent, version)
            val astNodes = Parser(tokens, version)

            val totalChars = fileContent.length
            var processedChars = 0

            while (astNodes.hasNext()) {
                val statement = astNodes.next()

                val lintResult = Linter.lint(statement, linterRules, version)

                if (!lintResult.isValid()) {
                    errorList.add(lintResult)
                }

                val lexerProcessedChars = tokens.getProcessedCharacters()
                ProgressTracker.updateProgress(processedChars, totalChars)

                processedChars = lexerProcessedChars
            }

            val noErrors = errorList.isEmpty()
            return if (noErrors) {
                "No problems found"
            } else {
                val errorMessages = errorList.mapIndexed { index, error ->
                    "Error ${index + 1}:\n${error.message}"
                }
                errorMessages.joinToString(separator = "\n\n")
            }
        } catch (e: Exception) {
            return "Error during analysis: ${e.message}"
        }
    }
}
