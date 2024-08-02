package org.example

object TokenGenerator {
    fun generateToken(value: String, row : Int, symbolNumber: Int): Token {
        return Token(getTypeFromValue(value), value, Position(row, symbolNumber))
    }

    private fun getTypeFromValue(value: String): TokenType {
        TODO("Not yet implemented")
    }
}