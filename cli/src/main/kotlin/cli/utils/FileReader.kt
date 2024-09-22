package cli.utils

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import rules.FormattingRules
import rules.LinterRules
import rules.LinterRulesV1
import rules.LinterRulesV2
import java.io.InputStream

object FileReader {
    fun getFileContents(
        fileName: String,
        version: String
    ): String {
        val fileLocation = getFileLocation(fileName, version)
        return try {
            val inputStream: InputStream? = FileReader::class.java.classLoader.getResourceAsStream(fileLocation)
            inputStream?.bufferedReader()?.use { it.readText() } ?: "File not found."
        } catch (e: Exception) {
            "Error reading file: ${e.message}"
        }
    }

    fun getFormattingRules(file: String, version: String): FormattingRules {
        val fileLocation = getFileLocation(file, version)
        return try {
            val inputStream: InputStream? = FileReader::class.java.classLoader.getResourceAsStream(fileLocation)
            if (inputStream != null) {
                val yamlMapper = YAMLMapper()
                yamlMapper.readValue(inputStream, FormattingRules::class.java)
            } else {
                throw Exception("File not found.")
            }
        } catch (e: Exception) {
            throw Exception("Error reading file: ${e.message}")
        }
    }

    fun getFileLocation(
        file: String,
        version: String
    ): String {
        return "ps/$version/$file"
    }

    fun getLinterRules(rulesFile: String, version: String): LinterRules {
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
