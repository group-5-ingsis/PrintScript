import interpreter.Interpreter
import lexer.Lexer
import parser.SyntacticParser
import parser.exceptions.SemanticErrorException
import token.Token
import kotlin.test.Test
import kotlin.test.assertFailsWith

class SemanticParserTester {
    @Test
    fun testAssignDeclareStatementShouldPass() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser()
        val interpreter = Interpreter
        val tokens: List<Token> = lexer.lex("let a: Number = 23;", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
    }

    @Test
    fun testAssignDeclareStatementShouldFail() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser()
        val interpreter = Interpreter
        val tokens: List<Token> = lexer.lex("let a: String = 23;", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        assertFailsWith(SemanticErrorException::class) {
            interpreter.interpret(ast)
        }
    }

    // PROBLEMA: HACE FALTA QUE SE EJECUTE LA DECLARACION PARA VALIDAR OTRAS DECLARACIONES.
    @Test
    fun testDeclarationStatementShouldFail() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser()
        val interpreter = Interpreter
        val tokens: List<Token> = lexer.lex("let a: String; let a: Number;", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        assertFailsWith(SemanticErrorException::class) {
            interpreter.interpret(ast)
        }
    }

    @Test
    fun testDeclarationStatementShouldPass() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser()
        val interpreter = Interpreter
        val tokens: List<Token> = lexer.lex("let a: String;", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
    }

    @Test
    fun testAssignationStatementShouldPass() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser()
        val interpreter = Interpreter
        val tokens: List<Token> = lexer.lex("let a: String; a = 'testing';", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
    }

    @Test
    fun testAssignationToConstShouldFail() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser()
        val interpreter = Interpreter
        val tokens: List<Token> = lexer.lex("const a: String = 'testing'; a = 'validator';", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        assertFailsWith(SemanticErrorException::class) {
            interpreter.interpret(ast)
        }
    }

    @Test
    fun testAssignationToNonExistentVariableShouldFail() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser()
        val interpreter = Interpreter
        val tokens: List<Token> = lexer.lex("let a: String; a = b;", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        assertFailsWith(SemanticErrorException::class) {
            interpreter.interpret(ast)
        }
    }

    @Test
    fun testAssignationToDifferentDeclaredTypeShouldFail() {
        val lexer = Lexer
        val syntaxParser = SyntacticParser()
        val interpreter = Interpreter
        val tokens: List<Token> = lexer.lex("let a: String; a = 22;", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        assertFailsWith(SemanticErrorException::class) {
            interpreter.interpret(ast)
        }
    }
}
