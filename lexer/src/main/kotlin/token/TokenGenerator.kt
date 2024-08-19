package token

import Position

object TokenGenerator {

    fun generateToken(value: String, row: Int, symbolIndex: Int): Token {
        val type = getTypeFromValue(value)
        val position = Position(row + 1, symbolIndex + 1)

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

        // cambiar de lugar -> Interpreter o StaticCodeAnalyzer
        val isValidVariableName = TypesMapGenerator.isValidVariableName(value)
        if (isValidVariableName) {
            return "IDENTIFIER"
        }

        return "UNKNOWN"
    }
}
