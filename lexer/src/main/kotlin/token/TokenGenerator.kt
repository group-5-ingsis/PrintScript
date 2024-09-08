package token

import position.Position

object TokenGenerator {
    fun generateToken(
        value: String,
        row: Int,
        symbolIndex: Int
    ): Token {
        val type = getTypeFromValue(value)
        val position = Position(row + 1, symbolIndex + 1)

        if (type == "UNKNOWN") {
            throw IllegalArgumentException("Unknown symbol $value in line ${position.line} index ${position.symbolIndex}")
        }

        return Token(value, type, position)
    }

    fun getTypeFromValue(value: String): String {
        val typesMap = TypesMapGenerator.getTypesMap()

        for ((pattern, type) in typesMap) {
            val patternInMap = Regex(pattern)
            val hasType = value.matches(patternInMap)

            if (hasType) {
                return type
            }
        }

        val variableNamePattern = "^[a-zA-Z_][a-zA-Z0-9_]*$"
        if (value.matches(Regex(variableNamePattern))) {
            return "IDENTIFIER"
        }

        return "UNKNOWN"
    }

    fun generateTokenFromBuffer(buffer: StringBuilder, row: Int, index: Int): Token {
        val value = buffer.toString()
        val token = TokenGenerator.generateToken(value, row, index - value.length)
        return token
    }
}
