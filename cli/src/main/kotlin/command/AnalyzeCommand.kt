package command

import cli.FileReader
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import lexer.Lexer
import linter.Linter
import parser.Parser
import rules.LinterRules
import rules.LinterRulesV1

class AnalyzeCommand(private val file: String, private val version: String, private val rulesFile: String) : Command {
    private val fileContent = FileReader.getFileContents(file, version)
    private val rulesFileString = FileReader.getFileContents(rulesFile, version)

    override fun execute(): String {
        val toReturn = StringBuilder()

        val tokens = Lexer(fileContent, version)

        val asts = Parser(tokens, version)

        val linter = Linter(getLinterRules(rulesFileString, version))

        val linterResult = linter.lint(asts)

        if (linterResult.isValid()) {
            return "OK: No linting errors found"
        }

        toReturn.append("Linting errors found:\n")
        linter.getErrors().forEach {
            toReturn.append("${it.getMessage()}\n")
        }

        return toReturn.toString()
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
            else -> throw IllegalArgumentException("Unsupported version: $version")
        }
        return linterRules
    }

    private fun jsonToMap(jsonString: String): Map<String, Any> {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(jsonString)
    }
}
