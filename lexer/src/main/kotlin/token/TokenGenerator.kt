package token

import position.Position

object TokenGenerator {
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

        return Token(value, type, position)
    }

    fun getTypeFromValue(value: String): String {
        val typesMap = TypesMapGenerator.getTypesMap()

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
