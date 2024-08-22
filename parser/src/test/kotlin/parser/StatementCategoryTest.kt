package parser

import lexer.Lexer
import org.junit.Assert.assertThrows
import parser.statement.Statement
import parser.statement.StatementManager
import kotlin.test.Test
import kotlin.test.assertEquals

class StatementCategoryTest {
  @Test
  fun testAssignationDeclaration() {
    val testString = "let a: Number = 4;"
    val tokens = Lexer.lex(testString, listOf())

    val statement = Statement(tokens, "Unknown")

    val statements = listOf(statement)
    val categorizedStatements = StatementManager.categorize(statements)
    assertEquals("AssignDeclare", categorizedStatements[0].statementType)
  }

  @Test
  fun testAssignation() {
    val testString = "a = 3"
    val tokens = Lexer.lex(testString, listOf())

    val statement = Statement(tokens, "Unknown")

    val statements = listOf(statement)
    val categorizedStatements = StatementManager.categorize(statements)
    assertEquals("Assignation", categorizedStatements[0].statementType)
  }

  @Test
  fun testDeclaration() {
    val testString = "let a : String;"
    val tokens = Lexer.lex(testString, listOf())

    val statement = Statement(tokens, "Unknown")

    val statements = listOf(statement)
    val categorizedStatements = StatementManager.categorize(statements)
    assertEquals("Declaration", categorizedStatements[0].statementType)
  }

  @Test
  fun testErrorDeclaration() {
    val testString = "let a { String"
    val tokens = Lexer.lex(testString, listOf())

    val statement = Statement(tokens, "Unknown")

    val statements = listOf(statement)
    assertThrows(IllegalArgumentException::class.java) {
      StatementManager.categorize(statements)
    }
  }
}
