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
    fun testDeclarationInterpreter() {
        val tokens: List<Token> = Lexer.lex("let a: Number;", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
    }

    @Test
    fun testNumberAssignation(){
        val tokens: List<Token> = Lexer.lex("let a: Number;", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
        assertEquals("undefined", VariableTable.getVariable("a"))
    }


    @Test
    fun testStringAssignation(){
        val tokens: List<Token> = Lexer.lex("a = \"Hello World\";" , listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
        assertEquals("\"Hello World\"", VariableTable.getVariable("a"))
    }


    @Test
    fun testLiteralAssignation(){
        val tokens: List<Token> = Lexer.lex("a = b;" , listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
        assertEquals("b", VariableTable.getVariable("a"))
    }

    @Test
    fun testAssignationDeclaration(){
        val tokens: List<Token> = Lexer.lex("let a: Number = 2; a = 3; let b: String; b = \"Hello\";", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
        assertEquals(3, VariableTable.getVariable("a"))
        assertEquals("\"Hello\"", VariableTable.getVariable("b"))
    }

    @Test
    fun testMethodCallString() {
        val tokens: List<Token> = Lexer.lex("let a : String; a = \"Hello\"; println(a);", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
    }


    @Test
    fun testMethodCallNumber() {
        val tokens: List<Token> = Lexer.lex("let a : Number; a = 2; println(a);", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        interpreter.interpret(ast)
    }

}