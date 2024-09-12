package token

import position.Position

class TokenGenerator(private val version: String = "1.1") {
    fun generateToken(
        value: String,
        row: Int,
        symbolIndex: Int
    ): Token {
        val type = getTypeFromValue(value)
        val position = Position(row, symbolIndex)

        if (type == "UNKNOWN") {
            throw IllegalArgumentException("Unknown symbol $value in line ${position.line} index ${position.symbolIndex}")
        }

        if (type == "STRING") {
            return Token(removeQuotes(value), type, position)
        }

        return Token(value, type, position)
    }

    private fun removeQuotes(value: String): String {
        return value.substring(1, value.length - 1)
    }

    private fun getTypeFromValue(value: String): String {
        val typesMap = TypesMapGenerator.getTypesMap(version)

        for ((pattern, type) in typesMap) {
            val patternInMap = Regex(pattern)
            if (value.matches(patternInMap)) {
                return type
            }
        }

        val variableNamePattern = "^[a-zA-Z_][a-zA-Z0-9_]*$"
        if (value.matches(Regex(variableNamePattern))) {
            return "IDENTIFIER"
        }

        return "UNKNOWN"
    }
}
