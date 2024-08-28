package parser

import lexer.Lexer
import org.junit.Assert.assertThrows
import token.Token
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

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
        val syntaxParser = SyntacticParser()

        val tokens: List<Token> = Lexer.lex("let a: Number = 44534 + 3454;", listOf())

        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)

        // Preparación de los datos esperados
        val expectedNodeType = "ASSIGNATION_DECLARATION"
        val expectedDataType = Node.DataType(type = "NUMBER") // Asegúrate de que este valor coincida con tu implementación
        val expectedKindVariableDeclaration = "let"
        val expectedIdentifier = "a"
        val expectedValueNodeType = "BINARY_OPERATION"

        // Obtención del primer hijo del AST, asumiendo que es el nodo de interés
        val actualNode = ast.getChildren()[0] as Node.AssignationDeclaration

        // Verificación de las propiedades del nodo
        assertEquals(expectedNodeType, actualNode.nodeType)
        assertEquals(expectedDataType.type, actualNode.dataType.type)
        assertEquals(expectedKindVariableDeclaration, actualNode.kindVariableDeclaration)
        assertEquals(expectedIdentifier, actualNode.identifier)
        assertEquals(expectedValueNodeType, actualNode.value.nodeType)
    }

    @Test
    fun testStringOperation() {
        val syntaxParser = SyntacticParser()

        val tokens: List<Token> = Lexer.lex("let a: String = 'Hello' + 'World';", listOf())

        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)

        // Preparación de los datos esperados
        val expectedNodeType = "ASSIGNATION_DECLARATION"
        val expectedDataType = Node.DataType(type = "STRING") // Asegúrate de que este valor coincida con tu implementación
        val expectedKindVariableDeclaration = "let"
        val expectedIdentifier = "a"
        val expectedValue = "BINARY_OPERATION"

        val actualNode = ast.getChildren()[0] as Node.AssignationDeclaration // Reemplaza esto con la lógica real para obtener el primer hijo

        assertEquals(expectedNodeType, actualNode.nodeType)
        assertEquals(expectedDataType.type, actualNode.dataType.type)
        assertEquals(expectedKindVariableDeclaration, actualNode.kindVariableDeclaration)
        assertEquals(expectedIdentifier, actualNode.identifier)
        assertEquals(expectedValue, actualNode.value.nodeType)
    }

    @Test
    fun testTokenSplittingBySemicolon() {
        val lexer = Lexer
        val tokens: List<Token> = lexer.lex("println(23);", listOf())
        println(getTokenSublist(tokens))
    }

    @Test
    fun testBuildDeclarationAST() {
        val syntaxParser = SyntacticParser()

        val tokens: List<Token> = Lexer.lex("let a: Number;", listOf())

        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)

        // Preparación de los datos esperados para una declaración simple
        val expectedNodeType = "DECLARATION"
        val expectedDataType = Node.DataType(type = "NUMBER") // Asegúrate de que este valor coincida con tu implementación
        val expectedKindVariableDeclaration = "let"
        val expectedIdentifier = "a"

        // Obtención del primer hijo del AST, asumiendo que es el nodo de interés
        val actualNode = ast.getChildren()[0] as Node.Declaration

        assertEquals(expectedNodeType, actualNode.nodeType)
        assertEquals(expectedDataType.type, actualNode.dataType.type)
        assertEquals(expectedKindVariableDeclaration, actualNode.kindVariableDeclaration)
        assertEquals(expectedIdentifier, actualNode.identifier)
    }

    @Test
    fun testBuildAssignationASTWithNumber() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser()
        val tokens: List<Token> = lexer.lex("x = 4;", listOf())
        val result: SyntacticParser.RootNode = syntaxParser.run(tokens)

        val expectedNodeType = "ASSIGNATION"
        val expectedIdentifier = "x"
        val expectedValueNodeType = "LITERAL"
        val expectedValueType = "4"

        val actualNode = result.getChildren()[0] as Node.Assignation

        val asignationValue = actualNode.value as Node.GenericLiteral

        assertEquals(expectedNodeType, actualNode.nodeType)
        assertEquals(expectedIdentifier, actualNode.identifier.value)
        assertEquals(expectedValueNodeType, actualNode.value.nodeType)
        assertEquals(expectedValueType, asignationValue.value)
    }

    @Test
    fun testBuildAssignationASTWithString() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser()
        val tokens: List<Token> = lexer.lex("x = 'test';", listOf())
        val result: SyntacticParser.RootNode = syntaxParser.run(tokens)
        println(result.getChildren())
        for (node in result.getChildren()) {
            println(node)
        }
    }

    @Test
    fun testBuildAssignationASTWithVariable() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser()
        val tokens: List<Token> = lexer.lex("x = y;", listOf())
        val result: SyntacticParser.RootNode = syntaxParser.run(tokens)

        // Preparación de los datos esperados para una asignación con variable
        val expectedNodeType = "ASSIGNATION"
        val expectedIdentifier = "x"
        val expectedValueNodeType = "IDENTIFIER"
        val expectedValueIdentifier = "y"

        // Obtención del primer hijo del AST, asumiendo que es el nodo de interés
        val actualNode = result.getChildren()[0] as Node.Assignation

        // Verificación de las propiedades del nodo
        assertEquals(expectedNodeType, actualNode.nodeType)
        assertEquals(expectedIdentifier, actualNode.identifier.value)
        assertEquals(expectedValueNodeType, actualNode.value.nodeType)
        assertEquals(expectedValueIdentifier, (actualNode.value as Node.Identifier).value)
        // TODO(Modificar la estructura de los nodos para que tengan un value y se pueda sacar algo de todos, asi no hay que castearlos)
    }

    @Test
    fun testBuildAssignDeclareAST() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser()
        val tokens: List<Token> = lexer.lex("let a: Number = 7;", listOf())
        val result: SyntacticParser.RootNode = syntaxParser.run(tokens)
        println(result.getChildren())
        for (node in result.getChildren()) {
            println(node)
        }
    }

    @Test
    fun testBuildMethodCallAST() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser()
        val tokens: List<Token> = lexer.lex("println(3);", listOf())
        val result: SyntacticParser.RootNode = syntaxParser.run(tokens)

        val expected =
            Node.Method(
                arguments =
                Node.Arguments(
                    argumentsOfAnyTypes =
                    listOf(
                        Node.GenericLiteral(value = "3", dataType = Node.DataType(type = "NUMBER"))
                    )
                ),
                identifier = Node.Identifier(value = "println")
            )

        assertEquals(expected, result.getChildren().firstOrNull())
    }

    @Test
    fun testDeclarationWithoutColonShouldFail() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser()
        val tokens: List<Token> = lexer.lex("let a Number;", listOf())

        val exception =
            assertFailsWith<IllegalArgumentException> {
                val result: SyntacticParser.RootNode = syntaxParser.run(tokens)
            }

        // Verificar el mensaje de la excepción
        assertEquals("Error at line 1, column 3: Expected a ':' after 'let a', but got 'Number' instead", exception.message)
    }

    @Test
    fun testAssignDeclareWithDifferentTypesShouldPassSyntacticParser() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser()
        val tokens: List<Token> = lexer.lex("let a: Number = \"testing\";", listOf())

        val result: SyntacticParser.RootNode = syntaxParser.run(tokens)

        // Imprimir los nodos generados para verificar la estructura del AST
        println(result.getChildren())
        for (node in result.getChildren()) {
            println(node)
        }

        // Representación esperada del nodo de declaración y asignación
        val expected =
            Node.AssignationDeclaration(
                dataType = Node.DataType(type = "NUMBER"),
                kindVariableDeclaration = "let",
                identifier = "a",
                value = Node.GenericLiteral(value = "\"testing\"", dataType = Node.DataType(type = "STRING"))
            )

        // Verificar que el AST generado contenga la estructura esperada
        val generatedNode = result.getChildren().firstOrNull()
        assertNotNull(generatedNode, "El AST no debe estar vacío.")
        assertEquals(expected, generatedNode)
    }

    @Test
    fun testStatementEndError() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 7; println(a)", listOf())
        assertThrows(IllegalArgumentException::class.java) {
            SyntacticParser().run(tokens)
        }
    }
}
