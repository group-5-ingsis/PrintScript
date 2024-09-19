package command

import cli.FileReader
import cli.ProgressTracker
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import lexer.Lexer
import linter.Linter
import parser.Parser
import position.Position
import rules.LinterRules
import rules.LinterRulesV1
import rules.LinterRulesV2

class AnalyzeCommand(private val file: String, private val version: String, rulesFile: String) : Command {
    private val rulesFileString = FileReader.getFileContents(rulesFile, version)
    private var progress: Int = 0

    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)
        val totalCharacters = fileContent.length

        var lastProcessedPosition = Position(0, 0)

        try {
            val tokens = Lexer(fileContent, version)
            val astNodes = Parser(tokens, version)

            Linter.clearResults()
            var processedCharacters = 0

            while (astNodes.hasNext()) {
                val statement = astNodes.next()
                Linter.lint(statement, getLinterRules(rulesFileString, version))

                val endPosition = statement.position

                processedCharacters += ProgressTracker.calculateProcessedCharacters(fileContent, lastProcessedPosition, endPosition)
                lastProcessedPosition = endPosition

                ProgressTracker.updateProgress(processedCharacters, totalCharacters)
                progress = ProgressTracker.getProgress()
            }

            if (processedCharacters < totalCharacters) {
                processedCharacters = totalCharacters
                ProgressTracker.updateProgress(processedCharacters, totalCharacters)
                progress = ProgressTracker.getProgress()
            }

            return if (Linter.getErrors().isEmpty()) {
                "No problems found"
            } else {
                val errorMessages = Linter.getErrors().mapIndexed { index, error ->
                    "Error ${index + 1}:\n${error.getMessage()}"
                }
                errorMessages.joinToString(separator = "\n\n")
            }
        } catch (e: Exception) {
            return "Error during analysis: ${e.message}"
        }
    }

    override fun getProgress(): Int {
        return progress
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
