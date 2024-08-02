package org.example

object TokenGenerator {

    fun generateToken(value: String, row : Int, symbolNumber: Int): Token {

        val type = getTypeFromValue(value)
        val position = Position(row, symbolNumber)

        return Token(type, value, position)
    }


    fun getTypeFromValue(value: String): TokenType {

        val typesMap = getTypesMap()

        for ((pattern, type) in typesMap) {

            val patternInMap = Regex(pattern)
            val hasType = value.matches(patternInMap)

            if (hasType) {
                return type
            }

        }
        return TokenType.UNKNOWN
    }


    private fun getTypesMap(): Map<String, TokenType> {
        val assignmentPattern = "="
        val numberPattern = """^-?\d+(\.\d+)?$"""
        val stringPattern = """^".*"$"""
        val operatorPattern = """^[\+\-\*/]$"""
        val punctuationPattern = """^[,;:.()]$"""
        val declarationPattern = """\b(let|const)\b"""

        return mapOf(
            assignmentPattern to TokenType.ASSIGNMENT,
            numberPattern to TokenType.NUMBER,
            stringPattern to TokenType.STRING,
            operatorPattern to TokenType.OPERATOR,
            punctuationPattern to TokenType.PUNCTUATION,
            declarationPattern to TokenType.DECLARATION
        )

    }


    /*
            Should return the type of the token based on the value
            Ej: "1" -> TokenType.NUMBER
                ""hola"" -> TokenType.STRING
                "let" -> TokenType.ASSIGNMENT
                "+" -> TokenType.OPERATOR
         */
}