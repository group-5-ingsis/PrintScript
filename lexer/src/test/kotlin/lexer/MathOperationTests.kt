package lexer

import org.junit.Assert.assertEquals
import token.Position
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
      Token("x", "IDENTIFIER", Position(1, 0)),
      Token("+", "OPERATOR", Position(1, 2)),
      Token("y", "IDENTIFIER", Position(1, 4)),
      Token("-", "OPERATOR", Position(1, 6)),
      Token("z", "IDENTIFIER", Position(1, 7))
    )

    val tokens = Lexer.fromString(input)
    assertEquals(expected, collectTokens(tokens))
  }
}
