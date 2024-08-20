package token

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

object TokenTypeTest {
    @Test
    fun testTokenTypeLet() {
        val string = "let"
        val expected = "DECLARATION_KEYWORD"

        val actual = TokenGenerator.getTypeFromValue(string)
        assertEquals(expected, actual)
    }

    @Test
    fun testTokenTypeString() {
        val string = "\"car\""
        val expected = "STRING"

        val actual = TokenGenerator.getTypeFromValue(string)
        assertEquals(expected, actual)
    }

    @Test
    fun testTokenTypeNumber() {
        val string = "1"
        val expected = "NUMBER"

        val actual = TokenGenerator.getTypeFromValue(string)
        assertEquals(expected, actual)
    }

    @Test
    fun testTokenTypeOperator() {
        val string = "+"
        val expected = "OPERATOR"

        val actual = TokenGenerator.getTypeFromValue(string)
        assertEquals(expected, actual)
    }

    @Test
    fun testTokenTypePunctuation() {
        val string = ";"
        val expected = "PUNCTUATION"

        val actual = TokenGenerator.getTypeFromValue(string)
        assertEquals(expected, actual)
    }

    @Test
    fun testTokenTypeDeclaration() {
        val string = ":"
        val expected = "PUNCTUATION"

        val actual = TokenGenerator.getTypeFromValue(string)
        assertEquals(expected, actual)
    }

    @Test
    fun testTokenTypeNumberType() {
        val string = "Number"
        val expected = "VARIABLE_TYPE"

        val actual = TokenGenerator.getTypeFromValue(string)
        assertEquals(expected, actual)
    }

    @Test
    fun testTokenTypeStringType() {
        val string = "String"
        val expected = "VARIABLE_TYPE"

        val actual = TokenGenerator.getTypeFromValue(string)
        assertEquals(expected, actual)
    }
}
