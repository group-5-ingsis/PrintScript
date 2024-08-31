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
        val tokens: List<Token> = Lexer.lex("let a: Number;")
        val ast = Parser().run(tokens)
        val initialScope: Environment = Environment()
        val newScope = interpreter.interpret(ast, initialScope)
        assertEquals(null, newScope.get("a"))
    }

    @Test
    fun testDeclarationWithString() {
        val tokens: List<Token> = Lexer.lex("let a: String;")
        val ast = Parser().run(tokens)
        val initialScope: Environment = Environment()
        val newScope = interpreter.interpret(ast, initialScope)
        assertEquals(null, newScope.get("a"))
    }

    @Test
    fun testAssignationWithString() {
        val tokens: List<Token> = Lexer.lex("let a: String = \"Hello World\";")
        val ast = Parser().run(tokens)
        val initialScope: Environment = Environment()
        val newScope = interpreter.interpret(ast, initialScope)
        assertEquals("\"Hello World\"", newScope.get("a"))
    }

    @Test
    fun testAssignationWithNumber() {
        val tokens: List<Token> = Lexer.lex("let c : Number = 4; c = 2;")
        val ast = Parser().run(tokens)
        val initialScope: Environment = Environment()
        val newScope = interpreter.interpret(ast, initialScope)
        assertEquals(2, newScope.get("c"))
    }

    @Test
    fun testAssignationWithLiteral() {
        val tokens: List<Token> = Lexer.lex("let b: Number = 3; let a: Number = 7; a = b;")
        val ast = Parser().run(tokens)
        val initialScope: Environment = Environment()
        val newScope = interpreter.interpret(ast, initialScope)
        assertEquals(3, newScope.get("a"))
    }

    @Test
    fun testAssignationDeclarationWithNumber() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 2; a = 3;")
        val ast = Parser().run(tokens)
        val initialScope: Environment = Environment()
        val newScope = interpreter.interpret(ast, initialScope)
        assertEquals(3, newScope.get("a"))
    }

    @Test
    fun testAssignationDeclarationWithString() {
        val tokens: List<Token> = Lexer.lex("let b: String; b = 'Hello';")
        val ast = Parser().run(tokens)
        val initialScope: Environment = Environment()
        val newScope = interpreter.interpret(ast, initialScope)
        assertEquals("'Hello'", newScope.get("b"))
    }

//    @Test
//    fun testMethodCallWithString() {
//        val tokens: List<Token> = Lexer.lex("let a: String; a = \"Hello\"; println(a);")
//        val ast = Parser().run(tokens)
//        val scope: Environment = Environment()
//
//        assertTrue(interpreter.interpret(ast, scope).contains( "Hello"))
//        assertEquals("Hello", scope.get("a"))
//
//    }

    @Test
    fun testMethodCallWithNumber() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 4; println(a);")
        val ast = Parser().run(tokens)
        val initialScope: Environment = Environment()
        val newScope = interpreter.interpret(ast, initialScope)
        assertEquals(4, newScope.get("a"))

    }

    @Test
    fun testSumNumber() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 6 + 2 + 6;")
        val ast = Parser().run(tokens)
        val initialScope: Environment = Environment()
        val newScope = interpreter.interpret(ast, initialScope)
        assertEquals(14, newScope.get("a"))
    }

    @Test
    fun testDivisionNumber() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 6 / 2;")
        val ast = Parser().run(tokens)
        val initialScope: Environment = Environment()
        val newScope = interpreter.interpret(ast, initialScope)
        assertEquals(3, newScope.get("a"))
    }

    @Test
    fun testComplexExpression() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 6 / (2 + 5) - (5 * 6);")
        val ast = Parser().run(tokens)
        val initialScope: Environment = Environment()
        val newScope = interpreter.interpret(ast, initialScope)
        assertEquals(-30, newScope.get("a"))
    }

    @Test
    fun testSumWithIdentifier() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 6; let b: Number = a + 2;")
        val ast = Parser().run(tokens)
        val initialScope: Environment = Environment()
        val newScope = interpreter.interpret(ast, initialScope)
        assertEquals(8, newScope.get("b"))
    }

    @Test
    fun testBinaryOperationString() {
        val tokens: List<Token> = Lexer.lex("let a: String = 'Hello' + 'World';")
        val ast = Parser().run(tokens)
        val initialScope: Environment = Environment()
        val newScope = interpreter.interpret(ast, initialScope)
        assertEquals("'Hello''World'", newScope.get("a")) // Asegúrate de que la concatenación sea la correcta
    }

    @Test
    fun testAddingAssignations() {
        val tokens: List<Token> = Lexer.lex("let a: Number = 7; let b : Number = 8; let c : Number = a + 3 + b;")
        val ast = Parser().run(tokens)
        val initialScope: Environment = Environment()
        val newScope = interpreter.interpret(ast, initialScope)
        assertEquals(18, newScope.get("c"))
    }
}
