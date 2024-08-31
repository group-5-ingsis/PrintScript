package parser

import SemanticErrorException
import lexer.Lexer
import kotlin.test.Test
import kotlin.test.assertFailsWith

class SemanticParserTester {

    @Test
    fun testAssignDeclareStatementShouldPass() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser(Lexer.lex("let a: Number = 23;"))
        val semanticParser = SemanticParser()
        val ast: SyntacticParser.RootNode = syntaxParser.parse()
        semanticParser.run(ast)
    }

    @Test
    fun testAssignDeclareStatementShouldFail() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser(Lexer.lex("let a: String = 23;"))
        val semanticParser = SemanticParser()
        val ast: SyntacticParser.RootNode = syntaxParser.parse()
        assertFailsWith<SemanticErrorException> {
            semanticParser.run(ast)
        }
    }

    // PROBLEMA: HACE FALTA QUE SE EJECUTE LA DECLARACION PARA VALIDAR OTRAS DECLARACIONES.
    @Test
    fun testDeclarationStatementShouldFail() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser(Lexer.lex("let a: String; let a: Number;"))
        val semanticParser = SemanticParser()
        val ast: SyntacticParser.RootNode = syntaxParser.parse()
        assertFailsWith<SemanticErrorException> {
            semanticParser.run(ast)
        }
    }

    @Test
    fun testDeclarationStatementShouldPass() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser(Lexer.lex("let a: String;"))
        val semanticParser = SemanticParser()
        val ast: SyntacticParser.RootNode = syntaxParser.parse()
        semanticParser.run(ast)
    }

    @Test
    fun testAssignationStatementShouldPass() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser(Lexer.lex("let a: String; a = 'testing';"))
        val semanticParser = SemanticParser()
        val ast: SyntacticParser.RootNode = syntaxParser.parse()
        semanticParser.run(ast)
    }

    @Test
    fun testAssignationToConstShouldFail() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser(Lexer.lex("const a: String = 'testing'; a = 'validator';"))
        val semanticParser = SemanticParser()

        assertFailsWith<SemanticErrorException> {
            val ast: SyntacticParser.RootNode = syntaxParser.parse()
            semanticParser.run(ast)
        }
    }

    @Test
    fun testAssignationToNonExistentVariableShouldFail() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser(Lexer.lex("let a: String; a = b;"))
        val semanticParser = SemanticParser()

        assertFailsWith<SemanticErrorException> {
            val ast: SyntacticParser.RootNode = syntaxParser.parse()
            semanticParser.run(ast)
        }
    }

    @Test
    fun testAssignationToDifferentDeclaredTypeShouldFail() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser(Lexer.lex("let a: String; a = 22;"))
        val semanticParser = SemanticParser()
        val ast: SyntacticParser.RootNode = syntaxParser.parse()
        assertFailsWith<SemanticErrorException> {
            semanticParser.run(ast)
        }
    }
}
