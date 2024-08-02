package org.example

object TokenGenerator {
    fun generateToken(value: String, row : Int, symbolNumber: Int): Token {
        return Token(getTypeFromValue(value), value, Position(row, symbolNumber))
    }

    private fun getTypeFromValue(value: String): TokenType {
        /*
            Should return the type of the token based on the value
            Ej: "1" -> TokenType.NUMBER
                ""hola"" -> TokenType.STRING
                "let" -> TokenType.ASSIGNMENT
                "+" -> TokenType.OPERATOR
         */
        TODO("Not yet implemented")
    }
}