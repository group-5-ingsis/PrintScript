import interpreter.Interpreter
import lexer.Lexer
import parser.SyntacticParser
import token.Token
import visitor.VariableTable
import kotlin.test.Test
import kotlin.test.assertEquals

class InterpreterTest {

    private val syntaxParser = SyntacticParser()
    private val interpreter = Interpreter

    @Test
    fun testDeclarationWithNumber() {
        val tokens: List<Token> = Lexer.lex("let a: Number;", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
        assertEquals("undefined", VariableTable.getVariable("a"))
    }

    @Test
    fun testDeclarationWithString() {
        val tokens: List<Token> = Lexer.lex("let a: String;", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
        assertEquals("undefined", VariableTable.getVariable("a"))
    }

    @Test
    fun testAssignationWithString() {
        val tokens: List<Token> = Lexer.lex("a = \"Hello World\";", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
        assertEquals("\"Hello World\"", VariableTable.getVariable("a"))
    }

    @Test
    fun testAssignationWithNumber() {
        val tokens: List<Token> = Lexer.lex("a = 2;", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
        assertEquals(2, VariableTable.getVariable("a"))
    }

    @Test
    fun testAssignationWithLiteral() {
        val tokens: List<Token> = Lexer.lex("let b: Number = 3; let a: Number; a = b;", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
        assertEquals(3, VariableTable.getVariable("a"))
    }

    @Test
    fun testAssignationDeclarationWithNumber() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 2; a = 3;", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
        assertEquals(3, VariableTable.getVariable("a"))
    }

    @Test
    fun testAssignationDeclarationWithString() {
        val tokens: List<Token> = Lexer.lex("let b: String; b = \"Hello\";", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
        assertEquals("\"Hello\"", VariableTable.getVariable("b"))
    }

    @Test
    fun testMethodCallWithString() {
        val tokens: List<Token> = Lexer.lex("let a: String; a = \"Hello\"; println(a);", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
    }

    @Test
    fun testMethodCallWithNumber() {
        val tokens: List<Token> = Lexer.lex("let a: Number; a = 2; println(a);", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
    }
}
