package lexer

import org.junit.Assert.assertEquals
import token.Position
import token.Token
import kotlin.test.Test

class BasicSyntaxTests {

  private fun collectTokens(lexer: Lexer): List<Token> {
    val tokens = mutableListOf<Token>()
    while (lexer.hasNext()) {
      tokens.add(lexer.next())
    }
    return tokens
  }

  @Test
  fun testLexSimpleVariableDeclaration() {
    val input = "let x = 10;"
    val expected = listOf(
      Token("let", "LET", Position(1, 1)),
      Token("x", "IDENTIFIER", Position(1, 5)),
      Token("=", "ASSIGNMENT", Position(1, 7)),
      Token("10", "NUMBER", Position(1, 9)),
      Token(";", "PUNCTUATION", Position(1, 10))
    )

    val tokens = Lexer.create(input)
    assertEquals(expected, collectTokens(tokens))
  }

  @Test
  fun testLexMultiLineVariableDeclaration() {
    val input = "let x = 10;\nlet y = 20;"
    val expected = listOf(
      Token("let", "LET", Position(1, 1)),
      Token("x", "IDENTIFIER", Position(1, 5)),
      Token("=", "ASSIGNMENT", Position(1, 7)),
      Token("10", "NUMBER", Position(1, 9)),
      Token(";", "PUNCTUATION", Position(1, 10)),
      Token("let", "LET", Position(2, 1)),
      Token("y", "IDENTIFIER", Position(2, 5)),
      Token("=", "ASSIGNMENT", Position(2, 7)),
      Token("20", "NUMBER", Position(2, 9)),
      Token(";", "PUNCTUATION", Position(2, 10))
    )

    val tokens = Lexer.create(input)
    assertEquals(expected, collectTokens(tokens))
  }
}
