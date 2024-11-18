package token

import kotlin.test.Test
import kotlin.test.assertEquals

class TokenGeneratorTest {

  private val tokenGenerator = TokenGenerator("1.0")

  @Test
  fun testGenerateTokenDeclarationKeyword() {
    val input = "let"
    val expected = Token("let", "DECLARATION_KEYWORD", Position(1, 1))
    val position = Position(1, 1)
    val actual = tokenGenerator.generateToken(input, position)
    assertEquals(expected, actual)
  }

  @Test
  fun testGenerateTokenIdentifier() {
    val input = "x"
    val expected = Token("x", "IDENTIFIER", Position(1, 1))
    val position = Position(1, 1)
    val actual = tokenGenerator.generateToken(input, position)
    assertEquals(expected, actual)
  }

  @Test
  fun testGenerateTokenNumber() {
    val input = "10"
    val expected = Token("10", "NUMBER", Position(1, 1))
    val position = Position(1, 1)
    val actual = tokenGenerator.generateToken(input, position)
    assertEquals(expected, actual)
  }

  @Test
  fun testGenerateTokenStringWithQuotes() {
    val input = "\"hello\""
    val expected = Token("hello", "STRING", Position(1, 1))
    val position = Position(1, 1)
    val actual = tokenGenerator.generateToken(input, position)
    assertEquals(expected, actual)
  }

  @Test
  fun testGenerateTokenAssignment() {
    val input = "="
    val expected = Token("=", "ASSIGNMENT", Position(1, 1))
    val position = Position(1, 1)
    val actual = tokenGenerator.generateToken(input, position)
    assertEquals(expected, actual)
  }
}
