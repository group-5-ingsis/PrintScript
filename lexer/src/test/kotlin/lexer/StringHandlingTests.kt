package lexer

import org.junit.Assert.assertEquals
import token.Position
import token.Token
import kotlin.test.Test

class StringHandlingTests {

  private fun collectTokens(lexer: Lexer): List<Token> {
    val tokens = mutableListOf<Token>()
    while (lexer.hasNext()) {
      tokens.add(lexer.next())
    }
    return tokens
  }

  @Test
  fun testLexDoubleQuotedString() {
    val input = "let str = \"Hello\""
    val expected = listOf(
      Token("let", "DECLARATION", Position(1, 0)),
      Token("str", "IDENTIFIER", Position(1, 4)),
      Token("=", "ASSIGNMENT", Position(1, 8)),
      Token("Hello", "STRING", Position(1, 9))
    )

    val tokens = Lexer.fromString(input)
    assertEquals(expected, collectTokens(tokens))
  }

  @Test
  fun testLexSingleQuotedString() {
    val input = "let x = 'hello'"
    val expected = listOf(
      Token("let", "DECLARATION", Position(1, 0)),
      Token("x", "IDENTIFIER", Position(1, 4)),
      Token("=", "ASSIGNMENT", Position(1, 6)),
      Token("hello", "STRING", Position(1, 7))
    )

    val tokens = Lexer.fromString(input)
    assertEquals(expected, collectTokens(tokens))
  }
}
