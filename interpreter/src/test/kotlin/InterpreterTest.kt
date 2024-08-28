import interpreter.Interpreter
import lexer.Lexer
import token.Token
import visitor.VariableTable
import kotlin.test.Test
import kotlin.test.assertEquals

class InterpreterTest {
    private val parser = SyntacticParser()
    private val interpreter = Interpreter

    @Test
    fun testDeclarationWithNumber() {
        val tokens: List<Token> = Lexer.lex("let a: Number;", listOf())
        val ast = parser.run(tokens)
        interpreter.interpret(ast)
        assertEquals("undefined", VariableTable.getVariable("a"))
    }

//    @Test
//    fun testDeclarationWithString() {
//        val tokens: List<Token> = Lexer.lex("let a: String;", listOf())
//        val ast = parser.run(tokens)
//        interpreter.interpret(ast)
//        assertEquals("undefined", VariableTable.getVariable("a"))
//    }
//
//    @Test
//    fun testAssignationWithString() {
//        val tokens: List<Token> = Lexer.lex("a = \"Hello World\";", listOf())
//        val ast = parser.run(tokens)
//        interpreter.interpret(ast)
//        assertEquals("\"Hello World\"", VariableTable.getVariable("a"))
//    }
//
//    @Test
//    fun testAssignationWithNumber() {
//        val tokens: List<Token> = Lexer.lex("a = 2;", listOf())
//        val ast = parser.run(tokens)
//        interpreter.interpret(ast)
//        assertEquals(2, VariableTable.getVariable("a"))
//    }
//
//    @Test
//    fun testAssignationWithLiteral() {
//        val tokens: List<Token> = Lexer.lex("let b: Number = 3; let a: Number; a = b;", listOf())
//        val ast = parser.run(tokens)
//        interpreter.interpret(ast)
//        assertEquals(3, VariableTable.getVariable("a"))
//    }
//
//    @Test
//    fun testAssignationDeclarationWithNumber() {
//        val tokens: List<Token> = Lexer.lex("let a: Number = 2; a = 3;", listOf())
//        val ast = parser.run(tokens)
//        interpreter.interpret(ast)
//        assertEquals(3, VariableTable.getVariable("a"))
//    }
//
//    @Test
//    fun testAssignationDeclarationWithString() {
//        val tokens: List<Token> = Lexer.lex("let b: String; b = \"Hello\";", listOf())
//        val ast = parser.run(tokens)
//        interpreter.interpret(ast)
//        assertEquals("\"Hello\"", VariableTable.getVariable("b"))
//    }
//
//    @Test
//    fun testMethodCallWithString() {
//        val tokens: List<Token> = Lexer.lex("let a: String; a = \"Hello\"; println(a);", listOf())
//        val ast = parser.run(tokens)
//        interpreter.interpret(ast)
//    }
//
//    @Test
//    fun testMethodCallWithNumber() {
//        val tokens: List<Token> = Lexer.lex("let a: Number = 4; println('Hello');", listOf())
//        val ast = parser.run(tokens)
//        interpreter.interpret(ast)
//    }
//
//    @Test
//    fun testSumNumber() {
//        val tokens: List<Token> = Lexer.lex("let a: Number = 6 + 2;", listOf())
//        val ast = parser.run(tokens)
//        interpreter.interpret(ast)
//        assertEquals(8.0, VariableTable.getVariable("a"))
//    }
//
//    @Test
//    fun testDivisionNumber() {
//        val tokens: List<Token> = Lexer.lex("let a: Number = 6 / 2;", listOf())
//        val ast = parser.run(tokens)
//        interpreter.interpret(ast)
//        assertEquals(3.0, VariableTable.getVariable("a"))
//    }
//
//    @Test
//    fun testSumWithIdentifier() {
//        val tokens: List<Token> = Lexer.lex("let a: Number = 6; let b: Number = a + 2;", listOf())
//        val ast = parser.run(tokens)
//        interpreter.interpret(ast)
//        assertEquals(8.0, VariableTable.getVariable("b"))
//    }
//
//    @Test
//    fun testBinaryOperationString() {
//        val tokens: List<Token> = Lexer.lex("let a: String = 'Hello' + 'World';", listOf())
//        val ast = parser.run(tokens)
//        interpreter.interpret(ast)
//        assertEquals("\'HelloWorld\'", VariableTable.getVariable("a"))
//    }

//  @Test
//  fun testAddingAssignations() {
//    val tokens: List<Token> = Lexer.lex("let a: Number = 7; let b : Number = 8; let c : Number = a + 3 + b;", listOf())
//    val ast = parser.run(tokens)
//    interpreter.interpret(ast)
//    assertEquals("18", VariableTable.getVariable("c"))
//  }
}
