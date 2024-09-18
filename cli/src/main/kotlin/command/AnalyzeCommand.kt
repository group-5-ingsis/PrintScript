package command

import cli.FileReader
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import lexer.Lexer
import linter.Linter
import parser.Parser
import rules.LinterRules
import rules.LinterRulesV1
import rules.LinterRulesV2
import kotlin.math.roundToInt

class AnalyzeCommand(private val file: String, private val version: String, private val rulesFile: String) : Command {
    private val fileContent = FileReader.getFileContents(file, version)
    private val rulesFileString = FileReader.getFileContents(rulesFile, version)
    private var progress: Int = 0 // To track progress

    override fun execute(): String {
        val toReturn = StringBuilder()

        val totalSteps = 3 // We have 3 main steps: Tokenizing, Parsing, and Linting
        var currentStep = 0

        try {
            // Step 1: Tokenizing
            val tokens = Lexer(fileContent, version)
            progress = (1.0 / totalSteps * 100).roundToInt()
            reportProgress(progress)

            // Step 2: Parsing
            val asts = Parser(tokens, version)
            progress = (2.0 / totalSteps * 100).roundToInt()
            reportProgress(progress)

            // Step 3: Linting
            val linter = Linter(getLinterRules(rulesFileString, version))
            val linterResult = linter.lint(asts)
            progress = 100
            reportProgress(progress)

            if (linterResult.isValid()) {
                return "OK: No linting errors found"
            }

            toReturn.append("Linting errors found:\n")
            linter.getErrors().forEach {
                toReturn.append("${it.getMessage()}\n")
            }

            return toReturn.toString()
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

    private fun reportProgress(progress: Int) {
        println("Progress: $progress%")
    }
}
