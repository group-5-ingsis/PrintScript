package lexer

import org.junit.Assert.assertEquals
import token.Position
import token.Token
import kotlin.test.Test

class TypeHandlingTests {

  private fun collectTokens(lexer: Lexer): List<Token> {
    val tokens = mutableListOf<Token>()
    while (lexer.hasNext()) {
      tokens.add(lexer.next())
    }
    return tokens
  }

  @Test
  fun testLexConstVariableDeclaration() {
    val input = "const pi: number = 3.14;"
    val expected = listOf(
      Token("const", "DECLARATION", Position(1, 0)),
      Token("pi", "IDENTIFIER", Position(1, 6)),
      Token(":", "PUNCTUATION", Position(1, 8)),
      Token("number", "VARIABLE_TYPE", Position(1, 10)),
      Token("=", "ASSIGNMENT", Position(1, 17)),
      Token("3.14", "NUMBER", Position(1, 19)),
      Token(";", "PUNCTUATION", Position(1, 23))
    )

    val tokens = Lexer.fromString(input)
    assertEquals(expected, collectTokens(tokens))
  }

  @Test
  fun testLexBooleanTypes() {
    val input = "let isTrue: boolean = true; isFalse = false;"
    val expected = listOf(
      Token("let", "DECLARATION", Position(1, 0)),
      Token("isTrue", "IDENTIFIER", Position(1, 4)),
      Token(":", "PUNCTUATION", Position(1, 10)),
      Token("boolean", "VARIABLE_TYPE", Position(1, 12)),
      Token("=", "ASSIGNMENT", Position(1, 20)),
      Token("true", "BOOLEAN", Position(1, 22)),
      Token(";", "PUNCTUATION", Position(1, 26)),
      Token("isFalse", "IDENTIFIER", Position(1, 28)),
      Token("=", "ASSIGNMENT", Position(1, 36)),
      Token("false", "BOOLEAN", Position(1, 38)),
      Token(";", "PUNCTUATION", Position(1, 43))
    )

    val tokens = Lexer.fromString(input)
    assertEquals(expected, collectTokens(tokens))
  }
}
