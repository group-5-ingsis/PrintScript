package lexer

import token.Token
import token.TokenGenerator

data class LexerState(
    val currentBuffer: StringBuilder = StringBuilder(),
    val currentRow: Int = 0,
    val currentIndex: Int = 0
) {
    fun advance(char: Char): LexerState {
        val newBuffer = StringBuilder(currentBuffer).append(char)
        val newIndex = currentIndex + 1
        return this.copy(currentBuffer = newBuffer, currentIndex = newIndex)
    }

    fun shouldGenerateToken(): Boolean {
        return currentBuffer.isNotEmpty()
    }

    fun generateTokenFromBuffer(): Token {
        val value = currentBuffer.toString()
        return TokenGenerator.generateToken(value, currentRow, currentIndex - value.length)
    }

    fun clearBuffer(): LexerState {
        return this.copy(currentBuffer = StringBuilder())
    }
}
