import interpreter.Interpreter
import lexer.Lexer
import org.junit.Assert.assertEquals
import org.junit.Test
import parser.Parser
import token.Token
import kotlin.test.assertTrue

class InterpreterTest {

    private val interpreter = Interpreter

    @Test
    fun testDeclarationWithNumber() {
        val tokens: List<Token> = Lexer.lex("let a: Number;", listOf())
        val ast = Parser().run(tokens)
        val scope: Environment = Environment()
        interpreter.interpret(ast, scope)
        assertEquals(null, scope.get("a"))
    }

    @Test
    fun testDeclarationWithString() {
        val tokens: List<Token> = Lexer.lex("let a: String;", listOf())
        val ast = Parser().run(tokens)
        val scope: Environment = Environment()
        interpreter.interpret(ast, scope)
        assertEquals(null, scope.get("a"))
    }

    @Test
    fun testAssignationWithString() {
        val tokens: List<Token> = Lexer.lex("let a: String = \"Hello World\";", listOf())
        val ast = Parser().run(tokens)
        val scope: Environment = Environment()
        interpreter.interpret(ast, scope)
        assertEquals("\"Hello World\"", scope.get("a"))
    }

    @Test
    fun testAssignationWithNumber() {
        val tokens: List<Token> = Lexer.lex("let c : Number = 4; c = 2;", listOf())

        val ast = Parser().run(tokens)
        val scope: Environment = Environment()
        interpreter.interpret(ast, scope)
        assertEquals(2, scope.get("c"))
    }

    @Test
    fun testAssignationWithLiteral() {
        val tokens: List<Token> = Lexer.lex("let b: Number = 3; let a: Number = 7; a = b;", listOf())
        val ast = Parser().run(tokens)
        val scope: Environment = Environment()
        interpreter.interpret(ast, scope)

        assertEquals(3, scope.get("a"))
    }

    @Test
    fun testAssignationDeclarationWithNumber() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 2; a = 3;", listOf())

        val ast = Parser().run(tokens)
        val scope: Environment = Environment()
        interpreter.interpret(ast, scope)

        assertEquals(3, scope.get("a"))
    }

    @Test
    fun testAssignationDeclarationWithString() {
        val tokens: List<Token> = Lexer.lex("let b: String; b = \"Hello\";", listOf())
        val ast = Parser().run(tokens)
        val scope: Environment = Environment()
        interpreter.interpret(ast, scope)

        assertEquals("hello", scope.get("a"))
    }

//    @Test
//    fun testMethodCallWithString() {
//        val tokens: List<Token> = Lexer.lex("let a: String; a = \"Hello\"; println(a);", listOf())
//        val ast = Parser().run(tokens)
//        val scope: Environment = Environment()
//
//        assertTrue(interpreter.interpret(ast, scope).contains( "Hello"))
//        assertEquals("Hello", scope.get("a"))
//
//    }

    @Test
    fun testMethodCallWithNumber() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 4; println(a);", listOf())
        val ast = Parser().run(tokens)
        val scope: Environment = Environment()
        assertTrue(interpreter.interpret(ast, scope).contains("4"))
    }

    @Test
    fun testSumNumber() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 6 + 2 + 6;", listOf())
        val ast = Parser().run(tokens)
        val scope: Environment = Environment()
        interpreter.interpret(ast, scope)

        assertEquals(14, scope.get("a"))
    }

    @Test
    fun testDivisionNumber() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 6 / 2;", listOf())
        val ast = Parser().run(tokens)
        val scope: Environment = Environment()
        interpreter.interpret(ast, scope)

        assertEquals(3, scope.get("a"))
    }

    @Test
    fun testComplexExpression() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 6 / (2 + 5) - (5 * 6) ;", listOf())
        val ast = Parser().run(tokens)
        val scope: Environment = Environment()
        interpreter.interpret(ast, scope)

        assertEquals(-30, scope.get("a"))
    }

    @Test
    fun testSumWithIdentifier() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 6; let b: Number = a + 2;", listOf())
        val ast = Parser().run(tokens)
        val scope: Environment = Environment()
        interpreter.interpret(ast, scope)
        assertEquals(8, scope.get("b"))
    }

    @Test
    fun testBinaryOperationString() {
        val tokens: List<Token> = Lexer.lex("let a: String = 'Hello' + 'World';", listOf())
        val ast = Parser().run(tokens)
        val scope: Environment = Environment()
        interpreter.interpret(ast, scope)
        assertEquals("\'Hello''World\'", scope.get("a"))
    }

    @Test
    fun testAddingAssignations() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 7; let b : Number = 8; let c : Number = a + 3 + b;", listOf())
        val ast = Parser().run(tokens)
        val scope: Environment = Environment()
        interpreter.interpret(ast, scope)
        assertEquals(18, scope.get("c"))
    }
}
