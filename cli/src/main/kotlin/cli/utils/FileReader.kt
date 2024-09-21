package cli.utils

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import rules.FormattingRules
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
}
