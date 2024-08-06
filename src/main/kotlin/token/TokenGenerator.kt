package token

import Position

object TokenGenerator {

    fun generateToken(value: String, row : Int, symbolNumber: Int): Token {

        val type = getTypeFromValue(value)
        val position = Position(row + 1, symbolNumber + 1)

        return Token(type, value, position)
    }


    fun getTypeFromValue(value: String): TokenType {

        val typesMap = TypesMapGenerator.getTypesMap()

        for ((pattern, type) in typesMap) {

            val patternInMap = Regex(pattern)
            val hasType = value.matches(patternInMap)

            if (hasType) {
                return type
            }

        }
        return TokenType.VARIABLENAME
    }

}