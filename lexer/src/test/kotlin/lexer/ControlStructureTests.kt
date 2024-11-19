package lexer

import org.junit.Assert.assertEquals
import token.Position
import token.Token
import kotlin.test.Test

class ControlStructureTests {

  private fun collectTokens(lexer: Lexer): List<Token> {
    val tokens = mutableListOf<Token>()
    while (lexer.hasNext()) {
      tokens.add(lexer.next())
    }
    return tokens
  }

  @Test
  fun testLexIfElseStatement() {
    val input = "if (true) { y = 5; } else { y = 0; }"
    val expected = listOf(
      Token("if", "CONDITIONAL", Position(1, 0)),
      Token("(", "PUNCTUATION", Position(1, 3)),
      Token("true", "BOOLEAN", Position(1, 4)),
      Token(")", "PUNCTUATION", Position(1, 8)),
      Token("{", "SCOPE", Position(1, 10)),
      Token("y", "IDENTIFIER", Position(1, 12)),
      Token("=", "ASSIGNMENT", Position(1, 14)),
      Token("5", "NUMBER", Position(1, 16)),
      Token(";", "PUNCTUATION", Position(1, 17)),
      Token("}", "SCOPE", Position(1, 19)),
      Token("else", "CONDITIONAL", Position(1, 21)),
      Token("{", "SCOPE", Position(1, 26)),
      Token("y", "IDENTIFIER", Position(1, 28)),
      Token("=", "ASSIGNMENT", Position(1, 30)),
      Token("0", "NUMBER", Position(1, 32)),
      Token(";", "PUNCTUATION", Position(1, 33)),
      Token("}", "SCOPE", Position(1, 35))
    )
    val tokens = Lexer.fromString(input, "1.1")
    assertEquals(expected, collectTokens(tokens))
  }
}
