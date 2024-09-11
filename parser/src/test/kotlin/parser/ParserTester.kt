package parser

import exceptions.BadSyntacticException
import lexer.Lexer
import nodes.Expression
import nodes.StatementType
import position.Position
import kotlin.test.*

class ParserTester {

    @Test
    fun testOperation() {
        val lexer = Lexer("let a : number = 3 + 5;", "1.0")
        val parser = Parser(lexer, "1.0")

        val ast1 = parser.next()

        val expectedAssignment = Expression.Binary(
            Expression.Literal(3, Position(1, 18)), // Adjusted for actual symbol index
            "+",
            Expression.Literal(5, Position(1, 22)), // Adjusted for actual symbol index
            Position(1, 19) // Adjusted for actual symbol index
        )

        val expectedNode = StatementType.Variable(
            "let",
            "a",
            expectedAssignment,
            "number",
            Position(1, 5) // Adjusted for actual symbol index
        )

        val actualNode = ast1 as StatementType.Variable

        assertEquals(expectedNode.identifier, actualNode.identifier)
        assertEquals(expectedNode.dataType, actualNode.dataType)

        val actualAssignment = actualNode.initializer as Expression.Binary
        assertEquals(expectedAssignment.left, actualAssignment.left)
        assertEquals(expectedAssignment.operator, actualAssignment.operator)
        assertEquals(expectedAssignment.right, actualAssignment.right)
        assertEquals(expectedAssignment.position, actualAssignment.position)
    }

    @Test
    fun testStringOperation() {
        val lexer = Lexer("let a: string = 'Hello' + 'World';", "1.0")
        val parser = Parser(lexer, "1.0")

        val ast1 = parser.next()

        val expectedLeftString = Expression.Literal("'Hello'", Position(1, 16))
        val expectedRightString = Expression.Literal("'World'", Position(1, 26))
        val expectedBinaryOperation = Expression.Binary(
            expectedLeftString,
            "+",
            expectedRightString,
            Position(1, 24)
        )
        val expectedNode = StatementType.Variable(
            "let",
            "a",
            expectedBinaryOperation,
            "string",
            Position(1, 1)
        )

        val actualNode = ast1 as StatementType.Variable

        assertEquals(expectedNode.identifier, actualNode.identifier)
        assertEquals(expectedNode.dataType, actualNode.dataType)

        val actualBinaryOperation = actualNode.initializer as Expression.Binary
        assertEquals(expectedBinaryOperation.left, actualBinaryOperation.left)
        assertEquals(expectedBinaryOperation.operator, actualBinaryOperation.operator)
        assertEquals(expectedBinaryOperation.right, actualBinaryOperation.right)
        assertEquals(expectedBinaryOperation.position, actualBinaryOperation.position)
    }

    @Test
    fun testBuildDeclarationAST() {
        val lexer = Lexer("let a: number;", "1.0")
        val parser = Parser(lexer, "1.0")

        val ast1 = parser.next()

        val expectedNode = StatementType.Variable(
            "let",
            "a",
            null, // No initializer
            "number",
            Position(1, 1)
        )

        val actualNode = ast1 as StatementType.Variable

        assertEquals(expectedNode.identifier, actualNode.identifier)
        assertEquals(expectedNode.dataType, actualNode.dataType)
        assertEquals(expectedNode.designation, actualNode.designation)
        assertNull(actualNode.initializer)
    }

    @Test
    fun testBuildAssignationAST() {
        val lexer = Lexer("let x : number = 3; x = 4;", "1.0")
        val parser = Parser(lexer, "1.0")

        val declaration = parser.next() as StatementType.Variable
        assertEquals("x", declaration.identifier)
        assertEquals("number", declaration.dataType)
        assertNotNull(declaration.initializer)
        assertEquals("LITERAL_EXPRESSION", (declaration.initializer as Expression.Literal).expressionType)

        val initializer = declaration.initializer as Expression.Literal
        assertEquals(3, initializer.value)

        val assignment = parser.next() as StatementType.StatementExpression
        val assignExpr = assignment.value as Expression.Assign
        assertEquals("x", assignExpr.name)
        assertEquals("ASSIGNMENT_EXPRESSION", assignExpr.expressionType)

        val assignValue = assignExpr.value as Expression.Literal
        assertEquals(4, assignValue.value)
        assertEquals("LITERAL_EXPRESSION", assignValue.expressionType)
    }

    @Test
    fun testAssignationWithVariable() {
        val lexer = Lexer("let x: number = 4; let y : number = 2; x = y;", "1.0")
        val parser = Parser(lexer, "1.0")

        val firstVariable = parser.next() as StatementType.Variable
        assertEquals("x", firstVariable.identifier)
        assertEquals("number", firstVariable.dataType)
        assertEquals("let", firstVariable.designation)
        assertEquals(4, (firstVariable.initializer as Expression.Literal).value)

        val secondVariable = parser.next() as StatementType.Variable
        assertEquals("y", secondVariable.identifier)
        assertEquals("number", secondVariable.dataType)
        assertEquals("let", secondVariable.designation)
        assertEquals(2, (secondVariable.initializer as Expression.Literal).value)

        val assignment = parser.next() as StatementType.StatementExpression
        val assignExpression = assignment.value as Expression.Assign
        assertEquals("x", assignExpression.name)
        val identifierExpression = assignExpression.value as Expression.Variable
        assertEquals("y", identifierExpression.name)
    }

    @Test
    fun testDeclarationWithoutColonShouldFail() {
        val lexer = Lexer("let a number;", "1.0")
        val parser = Parser(lexer, "1.0")

        val exception = assertFailsWith<BadSyntacticException> {
            parser.next()
        }

        assertEquals("Expected ':' after expression in Line 1, symbol 7", exception.message)
    }

    @Test
    fun testAssignDeclareWithDifferentTypesShouldPassSyntacticParser() {
        val lexer = Lexer("let a: string = 'testing';", "1.0")
        val parser = Parser(lexer, "1.0")

        val ast1 = parser.next()

        val expectedNode = StatementType.Variable(
            designation = "let",
            identifier = "a",
            initializer = Expression.Literal("'testing'", Position(1, 18)),
            dataType = "string",
            position = Position(1, 1)
        )

        val generatedNode = ast1 as StatementType.Variable
        assertNotNull(generatedNode, "El AST no debe estar vacío.")
        assertEquals(expectedNode.designation, generatedNode.designation)
        assertEquals(expectedNode.identifier, generatedNode.identifier)
        assertEquals(expectedNode.dataType, generatedNode.dataType)

        val initializer = generatedNode.initializer as? Expression.Literal
        assertNotNull(initializer, "El inicializador no debe ser nulo.")
        assertEquals("'testing'", initializer.value)
    }

    @Test
    fun statementSumElements() {
        val lexer = Lexer("let a: number = 5 + 3 + 4 / (6 + 6); println(a);", "1.0")
        val parser = Parser(lexer, "1.0")

        val firstStatement = parser.next() as StatementType.Variable
        assertEquals("a", firstStatement.identifier)
        assertEquals("number", firstStatement.dataType)
        assertEquals("let", firstStatement.designation)

        val expression = firstStatement.initializer as Expression.Binary
        assertTrue(expression.left is Expression.Binary)
        assertEquals("+", expression.operator)

        val leftExpression = expression.left as Expression.Binary
        assertEquals(5, (leftExpression.left as Expression.Literal).value)
        assertEquals("+", leftExpression.operator)
        assertEquals(3, (leftExpression.right as Expression.Literal).value)

        val rightExpression = expression.right as Expression.Binary
        assertEquals(4, (rightExpression.left as Expression.Literal).value)
        assertEquals("/", rightExpression.operator)

        val groupingInnerExpression = rightExpression.right as Expression.Grouping
        val groupingBinary = groupingInnerExpression.expression as Expression.Binary
        assertEquals(6, (groupingBinary.right as Expression.Literal).value)
        assertEquals(6, (groupingBinary.left as Expression.Literal).value)

        val secondStatement = parser.next() as StatementType.Print
        assertEquals("PRINT", secondStatement.statementType)
        assertEquals("a", (secondStatement.value.expression as Expression.Variable).name)
    }
}
