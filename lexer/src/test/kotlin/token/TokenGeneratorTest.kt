import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import token.Token
import token.TokenGenerator

class TokenGeneratorTest {

    @Test
    fun testGenerateTokenSimpleString() {
        val input = "let"
        val expected = Token("let", "DECLARATION_KEYWORD", Position(1, 1))
        val actual = TokenGenerator.generateToken(input, 0, 0)
        assertEquals(expected, actual)
    }

    @Test
    fun testGenerateTokenIdentifier() {
        val input = "x"
        val expected = Token("x", "IDENTIFIER", Position(1, 1))
        val actual = TokenGenerator.generateToken(input, 0, 0)
        assertEquals(expected, actual)
    }

    @Test
    fun testGenerateTokenNumber() {
        val input = "10"
        val expected = Token("10", "NUMBER", Position(1, 1))
        val actual = TokenGenerator.generateToken(input, 0, 0)
        assertEquals(expected, actual)
    }

    @Test
    fun testGenerateTokenStringWithQuotes() {
        val input = "\"hello\""
        val expected = Token("\"hello\"", "STRING", Position(1, 1))
        val actual = TokenGenerator.generateToken(input, 0, 0)
        assertEquals(expected, actual)
    }

    @Test
    fun testGenerateTokenOperator() {
        val input = "="
        val expected = Token("=", "ASSIGNMENT", Position(1, 1))
        val actual = TokenGenerator.generateToken(input, 0, 0)
        assertEquals(expected, actual)
    }

    @Test
    fun testGenerateTokenUnknown() {
        val input = "@"
        val expected = Token("@", "UNKNOWN", Position(1, 1))
        val actual = TokenGenerator.generateToken(input, 0, 0)
        assertEquals(expected, actual)
    }
}
