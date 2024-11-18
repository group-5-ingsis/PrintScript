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
      Token("if", "IF", Position(1, 1)),
      Token("(", "PUNCTUATION", Position(1, 4)),
      Token("true", "BOOLEAN", Position(1, 5)),
      Token(")", "PUNCTUATION", Position(1, 8)),
      Token("{", "LEFT_BRACE", Position(1, 11)),
      Token("y", "IDENTIFIER", Position(1, 13)),
      Token("=", "ASSIGNMENT", Position(1, 15)),
      Token("5", "NUMBER", Position(1, 17)),
      Token(";", "PUNCTUATION", Position(1, 17)),
      Token("}", "RIGHT_BRACE", Position(1, 20)),
      Token("else", "ELSE", Position(1, 22)),
      Token("{", "LEFT_BRACE", Position(1, 27)),
      Token("y", "IDENTIFIER", Position(1, 29)),
      Token("=", "ASSIGNMENT", Position(1, 31)),
      Token("0", "NUMBER", Position(1, 33)),
      Token(";", "PUNCTUATION", Position(1, 33)),
      Token("}", "RIGHT_BRACE", Position(1, 36))
    )

    val tokens = Lexer.fromString(input, "1.1")
    assertEquals(expected, collectTokens(tokens))
  }
}
