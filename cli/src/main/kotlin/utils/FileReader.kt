package utils

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import rules.FormattingRules
import rules.LinterRules
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
        val rulesMap = jsonToMap(rulesFile)

        val identifierNamingConvention = rulesMap["identifierNamingConvention"] as? String ?: "off"
        val printlnExpressionAllowed = rulesMap["printlnExpressionAllowed"] as? Boolean != false

        val readInputExpressionAllowed = if (version >= "1.1") {
            rulesMap["readInputExpressionAllowed"] as? Boolean
        } else {
            null
        }

        return LinterRules(
            version = version,
            identifierNamingConvention = identifierNamingConvention,
            printlnExpressionAllowed = printlnExpressionAllowed,
            readInputExpressionAllowed = readInputExpressionAllowed
        )
    }

    private fun jsonToMap(jsonString: String): Map<String, Any> {
        val mapper = jacksonObjectMapper()
        return mapper.readValue(jsonString)
    }
}
