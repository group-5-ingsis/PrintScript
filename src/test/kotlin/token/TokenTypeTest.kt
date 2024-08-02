package token

import org.example.TokenGenerator
import org.example.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

object TokenTypeTest {

    @Test
    fun testTokenTypeLet() {
        val string = "let"
        val expected = TokenType.DECLARATION

        val actual = TokenGenerator.getTypeFromValue(string)
        assertEquals(expected, actual)
    }

    @Test
    fun testTokenTypeString() {
        val string = "\"car\""
        val expected = TokenType.STRING

        val actual = TokenGenerator.getTypeFromValue(string)
        assertEquals(expected, actual)
    }

    @Test
    fun testTokenTypeNumber() {
        val string = "1"
        val expected = TokenType.NUMBER

        val actual = TokenGenerator.getTypeFromValue(string)
        assertEquals(expected, actual)
    }

    @Test
    fun testTokenTypeOperator() {
        val string = "+"
        val expected = TokenType.OPERATOR

        val actual = TokenGenerator.getTypeFromValue(string)
        assertEquals(expected, actual)
    }

    @Test
    fun testTokenTypePunctuation() {
        val string = ";"
        val expected = TokenType.PUNCTUATION

        val actual = TokenGenerator.getTypeFromValue(string)
        assertEquals(expected, actual)
    }

    @Test
    fun testTokenTypeDeclaration() {
        val string = ":"
        val expected = TokenType.DECLARATION

        val actual = TokenGenerator.getTypeFromValue(string)
        assertEquals(expected, actual)
    }



}

