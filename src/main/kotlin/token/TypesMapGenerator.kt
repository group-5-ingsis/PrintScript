package token

import java.io.File

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
            """^[,;.(){}:]$""" to "PUNCTUATION")
    }

    private fun getVariablePatternsMap(): Map<String, String> {

        val filePath = "src/main/resources/token_types.txt"

        val map = mutableMapOf<String, String>()

        File(filePath).forEachLine { line ->

            val parts = line.split("::")
            if (parts.size == 2) {

                val key = parts[0].trim()
                val values = parts[1].trim().split(",").map { it.trim() }

                val pattern = createPattern(values)

                if (key != "" && pattern.isNotEmpty()) {
                    map[pattern] = key
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