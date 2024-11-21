package parser

import environment.Environment
import lexer.Lexer
import nodes.Expression
import nodes.StatementType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import token.Position
import token.Token
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ParserNewVersionTesting {

  @Test
  fun testIfStm() {
    val lexer1 = Lexer.fromString("if (true) { println(3); }", "1.1")
    val parser1 = Parser(lexer1, "1.1")

    val ast1 = parser1.next()

    val expectedCondition1 = Expression.Literal(true, Position(1, 5))
    val expectedPrint1 = StatementType.Print(Expression.Grouping(Expression.Literal(3, Position(1, 21)), Position(1, 19)), Position(1, 11))
    val expectedBlock1 = StatementType.BlockStatement(Position(1, 19), listOf(expectedPrint1))
    val expectedIf1 = StatementType.IfStatement(Position(1, 10), expectedCondition1, expectedBlock1, null)

    val actualIf1 = ast1 as StatementType.IfStatement

    assertEquals(expectedIf1.condition, actualIf1.condition)
    assertEquals(expectedIf1.thenBranch.statementType, actualIf1.thenBranch.statementType)
    val contentExpectedThenBranch = (expectedIf1.thenBranch as StatementType.BlockStatement).listStm[0] as StatementType.Print
    val contentActualThenBranch = (actualIf1.thenBranch as StatementType.BlockStatement).listStm[0] as StatementType.Print
    assertEquals(contentExpectedThenBranch.value, contentActualThenBranch.value)
    assertEquals(expectedIf1.elseBranch, actualIf1.elseBranch)
  }

  @Test
  fun testIfElseStm() {
    val lexer = Lexer.fromString("if (false) { println(42); } else { println(0); }", "1.1")
    val parser = Parser(lexer, "1.1")

    val ast = parser.next()

    val expectedCondition = Expression.Literal(false, Position(1, 5))
    val expectedPrintThen = StatementType.Print(Expression.Grouping(Expression.Literal(42, Position(1, 22)), Position(1, 20)), Position(1, 11))
    val expectedThenBranch = StatementType.BlockStatement(Position(1, 19), listOf(expectedPrintThen))

    val expectedPrintElse = StatementType.Print(Expression.Grouping(Expression.Literal(0, Position(1, 44)), Position(1, 42)), Position(1, 32))
    val expectedElseBranch = StatementType.BlockStatement(Position(1, 44), listOf(expectedPrintElse))

    val expectedIf = StatementType.IfStatement(Position(1, 10), expectedCondition, expectedThenBranch, expectedElseBranch)

    val actualIf = ast as StatementType.IfStatement

    assertEquals(expectedIf.condition, actualIf.condition)
    assertEquals(expectedIf.thenBranch.statementType, actualIf.thenBranch.statementType)
    val expectedThenPrint = (expectedIf.thenBranch as StatementType.BlockStatement).listStm[0] as StatementType.Print
    val actualThenPrint = (actualIf.thenBranch as StatementType.BlockStatement).listStm[0] as StatementType.Print
    assertEquals(expectedThenPrint.value, actualThenPrint.value)

    val expectedElsePrint = (expectedIf.elseBranch as StatementType.BlockStatement).listStm[0] as StatementType.Print
    val actualElsePrint = (actualIf.elseBranch as StatementType.BlockStatement).listStm[0] as StatementType.Print
    assertEquals(expectedElsePrint.value, actualElsePrint.value)

    val expectedBlock1 = expectedIf.elseBranch as StatementType.BlockStatement
    val actualBlock2 = actualIf.elseBranch as StatementType.BlockStatement
    val expectedVariable = expectedBlock1.listStm[0] as StatementType.Print
    val actualVariable = actualBlock2.listStm[0] as StatementType.Print

    val expressionExpected = (expectedVariable.value).expression as Expression.Literal
    val actualExpression = (actualVariable.value).expression as Expression.Literal

    assertEquals(expressionExpected.value, actualExpression.value)
  }

  @Test
  fun testIfInsideIf() {
    val lexer = Lexer.fromString("if (true) { let a: boolean = true; if (a) {println(\" Hola! \"); }  } else { println(0); }", "1.1")
    val parser = Parser(lexer, "1.1")

    parser.next()
  }

  @Test
  fun testAndReturn() {
    val lexer = Lexer.fromString("let b : string  = \"hola \" ; print(\" b \" + \" Buenos dias! \"); if (true) { \n let a: boolean = true; \n if (a) {println(\" Hola! \"); }  } \n  else { println(0); }", "1.1")
    val parser = Parser(lexer, "1.1")
    parser.next()
  }

  @Test
  fun testIfWithVariableDeclaration() {
    val lexer = Lexer.fromString("let a: boolean = true; if (a) { let queso: number = 5; println(queso); }", "1.1")
    val parser = Parser(lexer, "1.1")

    val variableDeclaration = parser.next() as StatementType.Variable

    val expectedVariableA = StatementType.Variable(
      designation = "let",
      identifier = "a",
      initializer = Expression.Literal(true, Position(1, 18)),
      dataType = "boolean",
      position = Position(1, 4)
    )

    assertEquals(expectedVariableA.initializer, variableDeclaration.initializer)
    assertEquals(expectedVariableA.identifier, variableDeclaration.identifier)
    assertEquals(expectedVariableA.dataType, variableDeclaration.dataType)
    assertEquals(expectedVariableA.designation, variableDeclaration.designation)

    val ifStatement = parser.next() as StatementType.IfStatement

    val expectedCondition = Expression.Variable("a", Position(1, 28))

    val expectedVariableQueso = StatementType.Variable(
      designation = "let",
      identifier = "queso",
      initializer = Expression.Literal(5, Position(1, 53)),
      dataType = "number",
      position = Position(1, 37)
    )

    val expectedPrint = StatementType.Print(
      Expression.Grouping(Expression.Variable("queso", Position(1, 67)), Position(1, 61)),
      Position(1, 61)
    )

    val expectedBlock = StatementType.BlockStatement(
      position = Position(1, 31),
      listStm = listOf(expectedVariableQueso, expectedPrint)
    )

    val expectedIf = StatementType.IfStatement(
      position = Position(1, 27),
      condition = expectedCondition,
      thenBranch = expectedBlock,
      elseBranch = null
    )

    assertEquals(expectedIf.condition, ifStatement.condition)
    assertEquals(expectedIf.thenBranch.statementType, ifStatement.thenBranch.statementType)

    val expectedThenBranchList = (expectedIf.thenBranch as StatementType.BlockStatement).listStm
    val actualThenBranchList = (ifStatement.thenBranch as StatementType.BlockStatement).listStm

    val variableQuesoExpected = (expectedThenBranchList[0] as StatementType.Variable)
    val variableQuesoActual = (actualThenBranchList[0] as StatementType.Variable)
    assertEquals(((variableQuesoExpected.initializer) as Expression.Literal).value, (variableQuesoActual.initializer as Expression.Literal).value) // Variable queso
    assertEquals(variableQuesoExpected.identifier, variableQuesoActual.identifier)
    assertEquals(variableQuesoExpected.dataType, variableQuesoActual.dataType)
    assertEquals(variableQuesoExpected.designation, variableQuesoActual.designation)

    val printExpected = (expectedThenBranchList[1] as StatementType.Print)
    val printActual = (actualThenBranchList[1] as StatementType.Print)
    val expectedPrintValue = printExpected.value.expression as Expression.Variable
    val actualPrintValue = printActual.value.expression as Expression.Variable
    assertEquals(expectedPrintValue.name, actualPrintValue.name)
  }

  @Test
  fun `next should return a valid statement`() {
    val lexer = Lexer.fromString("  if (true){println(\"hola\");}", "1.1")
    val parser = Parser(lexer, "1.1")

    val statement = parser.next()

    assertNotNull(statement, "Expected next() to return a statement")
    assertTrue(statement is StatementType.IfStatement, "Expected an IfStatement")
  }

  @Test
  fun `hasNext should return true when lexer has tokens`() {
    val tokens = listOf(
      Token("IF", "IF_STATEMENT", Position(1, 0)),
      Token("ELSE", "ELSE_STATEMENT", Position(1, 2))
    )
    val parser = Parser(tokens.iterator())

    assertTrue(parser.hasNext(), "Expected hasNext() to return true")
  }

  @Test
  fun `hasNext should return false when lexer has no tokens`() {
    val parser = Parser(emptyList<Token>().iterator())

    assertFalse(parser.hasNext(), "Expected hasNext() to return false")
  }

  @Test
  fun `setEnv should update the environment`() {
    val parser = Parser(emptyList<Token>().iterator())
    val newEnv = Environment()

    parser.setEnv(newEnv)

    kotlin.test.assertEquals(newEnv, parser.getEnv(), "Expected environment to be updated")
  }

  @Test
  fun `next should throw NoSuchElementException when no tokens available`() {
    val parser = Parser(emptyList<Token>().iterator())

    assertThrows(NoSuchElementException::class.java) {
      parser.next()
    }
  }
}
