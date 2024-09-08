import interpreter.Interpreter
import lexer.Lexer
import org.junit.Assert.assertEquals
import org.junit.Test
import parser.Parser

class InterpreterTest {

    @Test
    fun testDeclarationWithNumber() {
        val tokens = Lexer("let a: Number;")
        val parser = Parser(tokens)
        val interpreter = Interpreter(parser) // Initialize Interpreter with parser
        val initialScope = Environment()
        val finalScope = interpreter.interpret(initialScope)
        assertEquals(null, finalScope.get("a"))
    }

    @Test
    fun testDeclarationWithString() {
        val tokens = Lexer("let a: String;")
        val parser = Parser(tokens)
        val interpreter = Interpreter(parser)
        val initialScope = Environment()
        val finalScope = interpreter.interpret(initialScope)
        assertEquals(null, finalScope.get("a"))
    }

    @Test
    fun testAssignationWithString() {
        val tokens = Lexer("let a: String = \"Hello World\" ;")
        val parser = Parser(tokens)
        val interpreter = Interpreter(parser)
        val initialScope = Environment()
        val newScope = interpreter.interpret(initialScope)
        assertEquals("\"Hello World\"", newScope.get("a"))
    }

    @Test
    fun testAssignationWithNumber() {
        val tokens = Lexer("let c : Number = 4; c = 2;")
        val parser = Parser(tokens)
        val interpreter = Interpreter(parser)
        val initialScope = Environment()
        val finalScope = interpreter.interpret(initialScope)
        assertEquals(2, finalScope.get("c"))
    }

    @Test
    fun testAssignationWithLiteral() {
        val tokens = Lexer("let b: Number = 3; let a: Number = 7; a = b;")
        val parser = Parser(tokens)
        val interpreter = Interpreter(parser)
        val initialScope = Environment()
        val finalScope = interpreter.interpret(initialScope)
        assertEquals(3, finalScope.get("a"))
    }

    @Test
    fun testAssignationDeclarationWithNumber() {
        val tokens = Lexer("let a: Number = 2; a = 3;")
        val parser = Parser(tokens)
        val interpreter = Interpreter(parser)
        val initialScope = Environment()
        val finalScope = interpreter.interpret(initialScope)
        assertEquals(3, finalScope.get("a"))
    }

    @Test
    fun testAssignationDeclarationWithString() {
        val tokens = Lexer("let b: String; b = 'Hello';")
        val parser = Parser(tokens)
        val interpreter = Interpreter(parser)
        val initialScope = Environment()
        val finalScope = interpreter.interpret(initialScope)
        assertEquals("'Hello'", finalScope.get("b"))
    }

    @Test
    fun testMethodCallWithNumber() {
        val tokens = Lexer("let a: Number = 4; println(a);")
        val parser = Parser(tokens)
        val interpreter = Interpreter(parser)
        val initialScope = Environment()
        val finalScope = interpreter.interpret(initialScope)
        assertEquals(4, finalScope.get("a"))
    }

    @Test
    fun testSumNumber() {
        val tokens = Lexer("let a: Number = 6 + 2 + 6;")
        val parser = Parser(tokens)
        val interpreter = Interpreter(parser)
        val initialScope = Environment()
        val finalScope = interpreter.interpret(initialScope)
        assertEquals(14, finalScope.get("a"))
    }

    @Test
    fun testDivisionNumber() {
        val tokens = Lexer("let a: Number = 6 / 2;")
        val parser = Parser(tokens)
        val interpreter = Interpreter(parser)
        val initialScope = Environment()
        val finalScope = interpreter.interpret(initialScope)
        assertEquals(3, finalScope.get("a"))
    }

    @Test
    fun testComplexExpression() {
        val tokens = Lexer("let a: Number = 6 / (2 + 5) - (5 * 6);")
        val parser = Parser(tokens)
        val interpreter = Interpreter(parser)
        val initialScope = Environment()
        val finalScope = interpreter.interpret(initialScope)
        assertEquals(-30, finalScope.get("a"))
    }

    @Test
    fun testSumWithIdentifier() {
        val tokens = Lexer("let a: Number = 6; let b: Number = a + 2;")
        val parser = Parser(tokens)
        val interpreter = Interpreter(parser)
        val initialScope = Environment()
        val finalScope = interpreter.interpret(initialScope)
        assertEquals(8, finalScope.get("b"))
    }

    @Test
    fun testBinaryOperationString() {
        val tokens = Lexer("let a: String = 'Hello' + 'World';")
        val parser = Parser(tokens)
        val interpreter = Interpreter(parser)
        val initialScope = Environment()
        val newScope = interpreter.interpret(initialScope)
        assertEquals("'Hello''World'", newScope.get("a"))
    }

    @Test
    fun testAddingAssignations() {
        val tokens = Lexer("let a: Number = 7; let b : Number = 8; let c : Number = a + 3 + b;")
        val parser = Parser(tokens)
        val interpreter = Interpreter(parser)
        val initialScope = Environment()
        val finalScope = interpreter.interpret(initialScope)
        assertEquals(18, finalScope.get("c"))
    }
}
