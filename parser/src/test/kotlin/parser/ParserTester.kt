package parser

import lexer.Lexer
import nodes.Expression
import nodes.Statement
import token.Position
import kotlin.test.*

class ParserTester {

  @Test
  fun testSimpleDeclaration() {
    val input = "let a : number = 3;"
    val version = "1.0"

    val lexer = Lexer.fromString(input, version)
    val parser = Parser(lexer, version)

    parser.next()
  }

  @Test
  fun testSimpleDeclarationAssignation() {
    val input = "let a : number = 3; a = 4"
    val version = "1.0"

    val lexer = Lexer.fromString(input, version)
    val parser = Parser(lexer, version)

    val ast1 = parser.next()
    val ast2 = parser.next()
    println(ast1)
    println(ast2)
  }

  @Test
  fun testOperation() {
    val lexer = Lexer.fromString("let a : number = 3 + 5;", "1.0")
    val parser = Parser(lexer, "1.0")

    val ast1 = parser.next()

    val expectedAssignment = Expression.Binary(
      Expression.Literal(3, Position(1, 18)),
      "+",
      Expression.Literal(5, Position(1, 22)),
      Position(1, 20)
    )

    val expectedNode = Statement.Variable(
      "let",
      "a",
      expectedAssignment,
      "number",
      Position(1, 5)
    )

    val actualNode = ast1 as Statement.Variable

    assertEquals(expectedNode.identifier, actualNode.identifier)
    assertEquals(expectedNode.dataType, actualNode.dataType)

    val actualAssignment = actualNode.initializer as Expression.Binary
    assertEquals(expectedAssignment.left, actualAssignment.left)
    assertEquals(expectedAssignment.operator, actualAssignment.operator)
    assertEquals(expectedAssignment.right, actualAssignment.right)
  }

  @Test
  fun testStringOperation() {
    val lexer = Lexer.fromString("let a: string = 'Hello' + 'World';", "1.0")
    val parser = Parser(lexer, "1.0")

    val ast1 = parser.next()

    val expectedLeftString = Expression.Literal("Hello", Position(1, 16))
    val expectedRightString = Expression.Literal("World", Position(1, 26))
    val expectedBinaryOperation = Expression.Binary(
      expectedLeftString,
      "+",
      expectedRightString,
      Position(1, 25)
    )
    val expectedNode = Statement.Variable(
      "let",
      "a",
      expectedBinaryOperation,
      "string",
      Position(1, 1)
    )

    val actualNode = ast1 as Statement.Variable

    assertEquals(expectedNode.identifier, actualNode.identifier)
    assertEquals(expectedNode.dataType, actualNode.dataType)

    val actualBinaryOperation = actualNode.initializer as Expression.Binary
    assertEquals(expectedBinaryOperation.left, actualBinaryOperation.left)
    assertEquals(expectedBinaryOperation.operator, actualBinaryOperation.operator)
    assertEquals(expectedBinaryOperation.right, actualBinaryOperation.right)
  }

  @Test
  fun mathOperation() {
    val lexer = Lexer.fromString("let a: number = 5 + 4 * 3 / 2;", "1.0")
    val parser = Parser(lexer, "1.0")

    parser.next()
  }

  @Test
  fun testBuildDeclarationAST() {
    val lexer = Lexer.fromString("let a: number;", "1.0")
    val parser = Parser(lexer, "1.0")

    val ast1 = parser.next()

    val expectedNode = Statement.Variable(
      "let",
      "a",
      null, // No initializer
      "number",
      Position(1, 1)
    )

    val actualNode = ast1 as Statement.Variable

    assertEquals(expectedNode.identifier, actualNode.identifier)
    assertEquals(expectedNode.dataType, actualNode.dataType)
    assertEquals(expectedNode.designation, actualNode.designation)
    assertNull(actualNode.initializer)
  }
}
