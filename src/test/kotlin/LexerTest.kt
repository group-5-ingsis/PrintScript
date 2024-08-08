import lexer.Lexer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import token.Token

class LexerTest {

  @Test
  fun testLexSimpleString() {
    val input = "let x = 10"
    val expected = listOf(
      Token("let", "ASSIGNATION_KEYWORD", Position(1, 1)),
      Token("x", "IDENTIFIER", Position(1, 2)),
      Token("=", "ASSIGNMENT", Position(1, 3)),
      Token("10", "NUMBER", Position(1, 4))
    )
    val actual = Lexer.lex(input, listOf())
    assertEquals(expected, actual)
  }

  @Test
  fun testLexStringWithQuotes() {
    val input = "let str = \"hello\""
    val expected = listOf(
      Token("let", "ASSIGNATION_KEYWORD", Position(1, 1)),
      Token("str", "IDENTIFIER", Position(1, 2)),
      Token("=", "ASSIGNMENT", Position(1, 3)),
      Token("\"hello\"", "STRING", Position(1, 4))
    )
    val actual = Lexer.lex(input, listOf())
    assertEquals(expected, actual)
  }

  @Test
  fun testLexStringWithSymbols() {
    val input = "x + y - z"
    val expected = listOf(
      Token("x", "IDENTIFIER", Position(1, 1)),
      Token("+", "OPERATOR", Position(1, 2)),
      Token("y", "IDENTIFIER", Position(1, 3)),
      Token("-", "OPERATOR", Position(1, 4)),
      Token("z", "IDENTIFIER", Position(1, 5))
    )
    val actual = Lexer.lex(input, listOf())
    assertEquals(expected, actual)
  }

  @Test
  fun testLexMultiLineString() {
    val input = "let x = 10\nlet y = 20"
    val expected = listOf(
      Token("let", "ASSIGNATION_KEYWORD", Position(1, 1)),
      Token("x", "IDENTIFIER", Position(1, 2)),
      Token("=", "ASSIGNMENT", Position(1, 3)),
      Token("10", "NUMBER", Position(1, 4)),
      Token("let", "ASSIGNATION_KEYWORD", Position(2, 1)),
      Token("y", "IDENTIFIER", Position(2, 2)),
      Token("=", "ASSIGNMENT", Position(2, 3)),
      Token("20", "NUMBER", Position(2, 4))
    )
    val actual = Lexer.lex(input, listOf())
    assertEquals(expected, actual)
  }
}