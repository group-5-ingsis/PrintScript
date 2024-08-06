package token

import java.io.File

object TypesMapGenerator {

    fun getTypesMap(): Map<String, TokenType> {

        val defaultPatternsMap = getDefaultPatternsMap()
        val variablePattensMap = getVariablePatternsMap()

        return defaultPatternsMap + variablePattensMap
    }

    private fun getDefaultPatternsMap(): Map<String, TokenType> {
        return mapOf(
            "=" to TokenType.ASSIGNMENT,
            """^-?\d+(\.\d+)?$""" to TokenType.NUMBER,
            """^".*"$""" to TokenType.STRING,
            """^[,;.(){}:]$""" to TokenType.PUNCTUATION)
    }

    private fun getVariablePatternsMap(): Map<String, TokenType> {

        val filePath = "src/main/resources/token_types.txt"

        val tokenTypeMapping = mapOf(
            "OPERATOR" to TokenType.OPERATOR,
            "VARIABLETYPE" to TokenType.VARIABLETYPE,
            "KEYWORD" to TokenType.KEYWORD
        )

        val map = mutableMapOf<String, TokenType>()

        File(filePath).forEachLine { line ->

            val parts = line.split("::")
            if (parts.size == 2) {

                val key = parts[0].trim()
                val values = parts[1].trim().split(",").map { it.trim() }

                val pattern = createPattern(values)

                val tokenType = tokenTypeMapping[key]

                if (tokenType != null && pattern.isNotEmpty()) {
                    map[pattern] = tokenType
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