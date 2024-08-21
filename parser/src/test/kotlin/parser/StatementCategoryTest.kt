package parser

import lexer.Lexer
import org.junit.jupiter.api.Test
import parser.statement.Statement
import parser.statement.StatementManager
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
  fun unknownStatement() {
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
}
