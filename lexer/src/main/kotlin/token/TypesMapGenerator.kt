package token

import java.io.BufferedReader
import java.io.InputStreamReader

object TypesMapGenerator {

    fun getTypesMap(): Map<String, String> {
        val defaultPatternsMap = getDefaultPatternsMap()
        val variablePattensMap = getVariablePatternsMap()

        return defaultPatternsMap + variablePattensMap
    }

    private fun getDefaultPatternsMap(): Map<String, String> {
        return mapOf(
            "=" to "ASSIGNMENT",
            """^-?\d+(\.\d+)?$""" to "NUMBER",
            """^".*"$""" to "STRING",
            """^[,;.(){}:]$""" to "PUNCTUATION"
        )
    }

    private fun getVariablePatternsMap(): Map<String, String> {
        val fileName = "token_types.txt"
        val map = mutableMapOf<String, String>()

        // Access the file as a resource from the classpath
        val inputStream = this::class.java.classLoader.getResourceAsStream(fileName)
            ?: throw IllegalArgumentException("Resource not found: $fileName")

        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            reader.forEachLine { line ->
                val parts = line.split("::")
                if (parts.size == 2) {
                    val key = parts[0].trim()
                    val values = parts[1].trim().split(",").map { it.trim() }

                    val pattern = createPattern(values)

                    if (key.isNotEmpty() && pattern.isNotEmpty()) {
                        map[pattern] = key
                    }
                }
            }
        }

        return map
    }

    private fun createPattern(symbols: List<String>): String {
        if (symbols.isEmpty()) return ""
        return "^(${symbols.joinToString("|")})$"
    }

    fun isValidVariableName(value: String): Boolean {
        val variableNamePattern = "^[a-zA-Z_][a-zA-Z0-9_]*$"
        return value.matches(Regex(variableNamePattern))
    }
}
