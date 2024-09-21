package command

import cli.utils.FileReader
import cli.utils.ProgressTracker
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import lexer.Lexer
import linter.Linter
import linter.LinterResult
import parser.Parser
import rules.LinterRules
import rules.LinterRulesV1
import rules.LinterRulesV2

class AnalyzeCommand(private val file: String, private val version: String, private val rulesFile: String) : Command {

    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)
        val rules = FileReader.getFileContents(rulesFile, version)

        val errorList = mutableListOf<LinterResult>()

        try {
            val lexer = Lexer(fileContent, version)
            val astNodes = Parser(lexer, version)

            val totalChars = fileContent.length
            var processedChars = 0

            while (astNodes.hasNext()) {
                val statement = astNodes.next()

                val lintResult = Linter.lint(statement, getLinterRules(rules, version))

                if (!lintResult.isValid()) {
                    errorList.add(lintResult)
                }

                val lexerProcessedChars = lexer.getProcessedCharacters()
                ProgressTracker.updateProgress(processedChars, totalChars)

                processedChars = lexerProcessedChars
            }

            if (fileContent.isNotEmpty()) {
                ProgressTracker.updateProgress(totalChars, totalChars)
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

    private fun getLinterRules(rulesFile: String, version: String): LinterRules {
        lateinit var linterRules: LinterRules
        when (version) {
            "1.0" -> {
                val rulesMap = jsonToMap(rulesFile)
                linterRules = LinterRulesV1(
                    identifierNamingConvention = rulesMap["identifierNamingConvention"] as String,
                    printlnExpressionAllowed = rulesMap["printlnExpressionAllowed"] as Boolean
                )
            }
            "1.1" -> {
                val rulesMap = jsonToMap(rulesFile)
                linterRules = LinterRulesV2(
                    identifierNamingConvention = rulesMap["identifierNamingConvention"] as String,
                    printlnExpressionAllowed = rulesMap["printlnExpressionAllowed"] as Boolean,
                    readInputExpressionAllowed = rulesMap["readInputExpressionAllowed"] as Boolean
                )
            }
            else -> throw IllegalArgumentException("Unsupported version: $version")
        }
        return linterRules
    }

    private fun jsonToMap(jsonString: String): Map<String, Any> {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(jsonString)
    }
}
