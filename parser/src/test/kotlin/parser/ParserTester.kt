package parser

import exceptions.BadSyntacticException
import lexer.Lexer
import nodes.Expression
import nodes.StatementType
import org.junit.Assert.assertNull
import parser.syntactic.SyntacticParser
import position.Position
import token.Token
import kotlin.test.*

class ParserTester {
    private fun getTokenSublist(tokens: List<Token>): List<List<Token>> {
        val tokenSublists = mutableListOf<List<Token>>()
        var j = 0
        for ((index, token) in tokens.withIndex()) {
            if (token.type == "PUNCTUATION" && token.value == ";") {
                tokenSublists.add(tokens.subList(j, index))
                j += index + 1
            }
        }
        return tokenSublists
    }

    @Test
    fun testOperation() {
        val tokens = Lexer.lex("let a : Number = 3 + 5;")
        val parser = SyntacticParser(tokens)
        val ast = parser.parse()

        val expectedAssignment = Expression.Binary(
            Expression.Literal(3, Position(1, 6)),
            "+",
            Expression.Literal(5, Position(1, 8)),
            Position(1, 7)
        )

        val expectedNode = StatementType.Variable(
            "let",
            "a",
            expectedAssignment,
            "Number",
            Position(1, 1)
        )

        // Verificamos que el primer hijo del AST sea del tipo esperado
        val actualNode = ast.getChildren()[0] as StatementType.Variable

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
        val tokens = Lexer.lex("let a: String = 'Hello' + 'World';")

        val parser = SyntacticParser(tokens)

        val ast = parser.parse()

        val expectedLeftString = Expression.Literal("'Hello'", Position(1, 6))
        val expectedRightString = Expression.Literal("'World'", Position(1, 8))
        val expectedBinaryOperation = Expression.Binary(
            expectedLeftString,
            "+",
            expectedRightString,
            Position(1, 7)
        )
        val expectedNode = StatementType.Variable(
            "let",
            "a",
            expectedBinaryOperation,
            "String",
            Position(1, 1)
        )

        // Verificamos que el primer hijo del AST sea del tipo esperado
        val actualNode = ast.getChildren()[0] as StatementType.Variable

        // Comprobamos el tipo de declaración, el identificador y el tipo de datos
        assertEquals(expectedNode.designation, actualNode.designation)
        assertEquals(expectedNode.identifier, actualNode.identifier)
        assertEquals(expectedNode.dataType, actualNode.dataType)

        // Verificamos la expresión de inicialización
        val actualBinaryOperation = actualNode.initializer as Expression.Binary
        assertEquals(expectedBinaryOperation.left, actualBinaryOperation.left)
        assertEquals(expectedBinaryOperation.operator, actualBinaryOperation.operator)
        assertEquals(expectedBinaryOperation.right, actualBinaryOperation.right)
        assertEquals(expectedBinaryOperation.position, actualBinaryOperation.position)
    }

    @Test
    fun testTokenSplittingBySemicolon() {
        val lexer = Lexer
        val tokens: List<Token> = lexer.lex("println(23);")
        println(getTokenSublist(tokens))
    }

    @Test
    fun testBuildDeclarationAST() {
        val syntaxParser = SyntacticParser(tokens = Lexer.lex("let a: Number;"))
        val ast: SyntacticParser.RootNode = syntaxParser.parse()
        val expectedNodeType = "VARIABLE_STATEMENT"
        val expectedDataType = "Number"
        val expectedKindVariableDeclaration = "let"
        val expectedIdentifier = "a"

        val actualNode = ast.getChildren()[0] as StatementType.Variable
        assertEquals(expectedNodeType, actualNode.statementType)
        assertEquals(expectedDataType, actualNode.dataType)
        assertEquals(expectedKindVariableDeclaration, actualNode.designation)
        assertEquals(expectedIdentifier, actualNode.identifier)
        assertNull(actualNode.initializer)
    }

    @Test
    fun testBuildAssignationAST() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser(tokens = Lexer.lex("let x : Number = 3; x = 4;"))

        val result: SyntacticParser.RootNode = syntaxParser.parse()

        val declaration = result.getChildren()[0] as StatementType.Variable
        assertEquals("x", declaration.identifier)
        assertEquals("Number", declaration.dataType)
        assertNotNull(declaration.initializer)
        assertEquals("LITERAL_EXPRESSION", declaration.initializer!!.expressionType)

        val initializer = declaration.initializer as Expression.Literal
        assertEquals(3, initializer.value)

        // Verificar la segunda declaración: "x = 4;"
        val assignment = result.getChildren()[1] as StatementType.StatementExpression
        val assignExpr = assignment.value as Expression.Assign
        assertEquals("x", assignExpr.name)
        assertEquals("ASSIGNMENT_EXPRESSION", assignExpr.expressionType)

        val assignValue = assignExpr.value as Expression.Literal
        assertEquals(4, assignValue.value)
        assertEquals("LITERAL_EXPRESSION", assignValue.expressionType)
    }

    @Test
    fun testAssignationWithVariable() {
        val lexer = Lexer
        val tokens: List<Token> = lexer.lex("let x: Number = 4; let y : Number = 2; x = y;")
        val result: SyntacticParser.RootNode = SyntacticParser(tokens).parse()

        // Assert
        val children = result.getChildren()
        assertEquals(3, children.size)

        // Check the first variable declaration and assignment
        val firstVariable = children[0] as StatementType.Variable
        assertEquals("x", firstVariable.identifier)
        assertEquals("Number", firstVariable.dataType)
        assertEquals("let", firstVariable.designation)
        assertEquals(4, (firstVariable.initializer as Expression.Literal).value)

        // Check the second variable declaration and assignment
        val secondVariable = children[1] as StatementType.Variable
        assertEquals("y", secondVariable.identifier)
        assertEquals("Number", secondVariable.dataType)
        assertEquals("let", secondVariable.designation)
        assertEquals(2, (secondVariable.initializer as Expression.Literal).value)

        // Check the assignment statement
        val assignment = children[2] as StatementType.StatementExpression
        val assignExpression = assignment.value as Expression.Assign
        assertEquals("x", assignExpression.name)
        val identifierExpression = assignExpression.value as Expression.Variable
        assertEquals("y", identifierExpression.name)
    }

    @Test
    fun testBuildMethodCallAST() {
        val lexer = Lexer

        val tokens: List<Token> = lexer.lex("println(4);")
        val syntaxParser = SyntacticParser(tokens)
        val result: SyntacticParser.RootNode = syntaxParser.parse()

        val expectedNodeType = "PRINT"
        val expectedLiteralValue = 4

        val printNode = result.getChildren()[0] as StatementType.Print

        val actualGroup = printNode.value

        assertTrue(actualGroup.expression is Expression.Literal)
        assertEquals(printNode.statementType, expectedNodeType)
        assertEquals((printNode.value.expression as Expression.Literal).value, expectedLiteralValue)
    }

    @Test
    fun testDeclarationWithoutColonShouldFail() {
        val lexer = Lexer

        val tokens: List<Token> = lexer.lex("let a Number;")
        val syntaxParser = SyntacticParser(tokens)
        // Verifica que se lance una excepción de tipo IllegalArgumentException cuando se ejecuta el parser
        val exception = assertFailsWith<BadSyntacticException> {
            syntaxParser.parse()
        }

        assertEquals("Expect: : after expression.", exception.message)
    }

    @Test
    fun testAssignDeclareWithDifferentTypesShouldPassSyntacticParser() {
        val lexer = Lexer

        val tokens: List<Token> = lexer.lex("let a: Number = 'testing';")
        val syntaxParser = SyntacticParser(tokens)
        val result: SyntacticParser.RootNode = syntaxParser.parse()

        println(result.getChildren())
        for (node in result.getChildren()) {
            println(node)
        }

        val expectedNode = StatementType.Variable(
            designation = "let",
            identifier = "a",
            initializer = Expression.Literal(value = "testing", position = Position(1, 18)),
            dataType = "Number",
            position = Position(1, 1)
        )

        val generatedNode = result.getChildren().firstOrNull() as? StatementType.Variable
        assertNotNull(generatedNode, "El AST no debe estar vacío.")
        assertEquals(expectedNode.designation, generatedNode.designation)
        assertEquals(expectedNode.identifier, generatedNode.identifier)
        assertEquals(expectedNode.dataType, generatedNode.dataType)

        // Verificar el valor inicializador
        val initializer = generatedNode.initializer as? Expression.Literal
        assertNotNull(initializer, "El inicializador no debe ser nulo.")
        assertEquals("'testing'", initializer.value)
    }

    @Test
    fun testStatementEndError() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 5; println(a);")
        println(Parser().run(tokens))
    }

    @Test
    fun statementSumElements() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 5 + 3 + 4 / (6 + 6); println(a);")
        val result = SyntacticParser(tokens).parse()

        // Assert
        val children = result.getChildren()
        assertEquals(2, children.size)

        val firstStatement = children[0] as StatementType.Variable
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

        val groupingExpression = rightExpression.right as Expression.Grouping
        val groupingInnerExpression = groupingExpression.expression as Expression.Binary
        assertEquals(6, (groupingInnerExpression.left as Expression.Literal).value)
        assertEquals("+", groupingInnerExpression.operator)
        assertEquals(6, (groupingInnerExpression.right as Expression.Literal).value)

        // Check the println statement
        val secondStatement = children[1] as StatementType.Print
        val printExpression = secondStatement.value.expression as Expression.Variable
        assertEquals("a", printExpression.name)
    }
}
