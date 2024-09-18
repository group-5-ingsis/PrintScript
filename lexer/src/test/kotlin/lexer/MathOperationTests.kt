package lexer

import org.junit.Assert.assertEquals
import position.Position
import token.Token
import kotlin.test.Test

class MathOperationTests {

    private fun collectTokens(lexer: Lexer): List<Token> {
        val tokens = mutableListOf<Token>()
        while (lexer.hasNext()) {
            tokens.add(lexer.next())
        }
        return tokens
    }

    @Test
    fun testLexMathOperations() {
        val input = "x + y - z"
        val expected = listOf(
            Token("x", "IDENTIFIER", Position(1, 1)),
            Token("+", "OPERATOR", Position(1, 3)),
            Token("y", "IDENTIFIER", Position(1, 5)),
            Token("-", "OPERATOR", Position(1, 7)),
            Token("z", "IDENTIFIER", Position(1, 8))
        )

        val tokens = Lexer(input)
        assertEquals(expected, collectTokens(tokens))
    }
}
