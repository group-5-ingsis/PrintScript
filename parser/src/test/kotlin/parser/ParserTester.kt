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
        val tokens = Lexer("let a : Number = 3 + 5;", "1.0")

        val asts = Parser(tokens)

        val parser = ParserBuilder().withVersion("1.0").build()
        val ast1 = parser.parse(tokens)

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
            "Number",
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
        val lexer = LexerBuilder().withInput("let a: String = 'Hello' + 'World';").build()
        val parser = ParserBuilder().withVersion("1.0").build()
        val tokens = lexer.tokenize()
        val ast1 = parser.parse(tokens)

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
            "String",
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
        val lexer = LexerBuilder().withInput("let a: Number;").build()
        val parser = ParserBuilder().withVersion("1.0").build()
        val tokens = lexer.tokenize()
        val ast1 = parser.parse(tokens)

        val expectedNodeType = "VARIABLE_STATEMENT"
        val expectedDataType = "Number"
        val expectedKindVariableDeclaration = "let"
        val expectedIdentifier = "a"

        val actualNode = ast1 as StatementType.Variable
        assertEquals(expectedNodeType, actualNode.statementType)
        assertEquals(expectedDataType, actualNode.dataType)
        assertEquals(expectedKindVariableDeclaration, actualNode.designation)
        assertEquals(expectedIdentifier, actualNode.identifier)
        assertNull(actualNode.initializer)
    }

    @Test
    fun testBuildAssignationAST() {
        val lexer = LexerBuilder().withInput("let x : Number = 3; x = 4;").build()
        val parser = ParserBuilder().withVersion("1.0").build()
        val tokens = lexer.tokenize()
        val ast1 = parser.parse(tokens)
        val ast2 = parser.parse(tokens)

        val declaration = ast1 as StatementType.Variable
        assertEquals("x", declaration.identifier)
        assertEquals("Number", declaration.dataType)
        assertNotNull(declaration.initializer)
        assertEquals("LITERAL_EXPRESSION", declaration.initializer!!.expressionType)

        val initializer = declaration.initializer as Expression.Literal
        assertEquals(3, initializer.value)

        val assignment = ast2 as StatementType.StatementExpression
        val assignExpr = assignment.value as Expression.Assign
        assertEquals("x", assignExpr.name)
        assertEquals("ASSIGNMENT_EXPRESSION", assignExpr.expressionType)

        val assignValue = assignExpr.value as Expression.Literal
        assertEquals(4, assignValue.value)
        assertEquals("LITERAL_EXPRESSION", assignValue.expressionType)
    }

    @Test
    fun testAssignationWithVariable() {
        val lexer = LexerBuilder().withInput("let x: Number = 4; let y : Number = 2; x = y;").build()
        val parser = ParserBuilder().withVersion("1.0").build()
        val tokens = lexer.tokenize()
        val ast1 = parser.parse(tokens)
        val ast2 = parser.parse(tokens)
        val ast3 = parser.parse(tokens)

        val firstVariable = ast1 as StatementType.Variable
        assertEquals("x", firstVariable.identifier)
        assertEquals("Number", firstVariable.dataType)
        assertEquals("let", firstVariable.designation)
        assertEquals(4, (firstVariable.initializer as Expression.Literal).value)

        val secondVariable = ast2 as StatementType.Variable
        assertEquals("y", secondVariable.identifier)
        assertEquals("Number", secondVariable.dataType)
        assertEquals("let", secondVariable.designation)
        assertEquals(2, (secondVariable.initializer as Expression.Literal).value)

        val assignment = ast3 as StatementType.StatementExpression
        val assignExpression = assignment.value as Expression.Assign
        assertEquals("x", assignExpression.name)
        val identifierExpression = assignExpression.value as Expression.Variable
        assertEquals("y", identifierExpression.name)
    }

    @Test
    fun testBuildMethodCallAST() {
        val lexer = LexerBuilder().withInput("println(4);").build()
        val parser = ParserBuilder().withVersion("1.0").build()
        val tokens = lexer.tokenize()
        val ast1 = parser.parse(tokens)

        val expectedNodeType = "PRINT"
        val expectedLiteralValue = 4

        val printNode = ast1 as StatementType.Print

        val actualGroup = printNode.value

        assertTrue(actualGroup.expression is Expression.Literal)
        assertEquals(printNode.statementType, expectedNodeType)
        assertEquals((printNode.value.expression as Expression.Literal).value, expectedLiteralValue)
    }

    @Test
    fun testDeclarationWithoutColonShouldFail() {
        val lexer = LexerBuilder().withInput("let a Number;").build()
        val parser = ParserBuilder().withVersion("1.0").build()
        val tokens = lexer.tokenize()
        val exception = assertFailsWith<BadSyntacticException> {
            parser.parse(tokens)
        }

        assertEquals("Expected ':' after expression in Line 1, symbol 7", exception.message)
    }

    @Test
    fun testAssignDeclareWithDifferentTypesShouldPassSyntacticParser() {
        val lexer = LexerBuilder().withInput("let a: Number = 'testing';").build()
        val parser = ParserBuilder().withVersion("1.0").build()
        val tokens = lexer.tokenize()
        val ast1 = parser.parse(tokens)

        val expectedNode = StatementType.Variable(
            designation = "let",
            identifier = "a",
            initializer = Expression.Literal(value = "testing", position = Position(1, 18)),
            dataType = "Number",
            position = Position(1, 1)
        )

        val generatedNode = ast1 as? StatementType.Variable
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
        val lexer = LexerBuilder().withInput("let a: Number = 5 + 3 + 4 / (6 + 6); println(a);").build()
        val parser = ParserBuilder().withVersion("1.0").build()
        val tokens = lexer.tokenize()
        val ast1 = parser.parse(tokens)
        val ast2 = parser.parse(tokens)

        val firstStatement = ast1 as StatementType.Variable
        assertEquals("a", firstStatement.identifier)
        assertEquals("Number", firstStatement.dataType)
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

        val groupingInnerExpression = rightExpression.right as Expression.Binary
        assertEquals(6, (groupingInnerExpression.left as Expression.Literal).value)
        assertEquals(6, (groupingInnerExpression.right as Expression.Literal).value)

        val secondStatement = ast2 as StatementType.Print
        assertEquals("PRINT", secondStatement.statementType)
        assertEquals("a", (secondStatement.value as Expression.Variable).name)
    }
}
