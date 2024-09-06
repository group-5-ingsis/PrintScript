import interpreter.Interpreter
import lexer.Lexer
import org.junit.Assert.assertEquals
import org.junit.Test
import parser.Parser

class InterpreterTest {

    private val interpreter = Interpreter

    @Test
    fun testDeclarationWithNumber() {
        val tokens = Lexer("let a: Number;")
        val parser = Parser(tokens)
        val ast1 = parser.next()
        val initialScope = Environment()
        val newScope = interpreter.interpret(ast1, initialScope)
        assertEquals(null, newScope.get("a"))
    }

    @Test
    fun testDeclarationWithString() {
        val tokens = Lexer("let a: String;")
        val parser = Parser(tokens)
        val ast1 = parser.next()
        val initialScope = Environment()
        val newScope = interpreter.interpret(ast1, initialScope)
        assertEquals(null, newScope.get("a"))
    }

    @Test
    fun testAssignationWithString() {
        val tokens = Lexer("let a: String = \"Hello World\" ;")
        val parser = Parser(tokens)
        val ast1 = parser.next()
        val initialScope = Environment()
        val newScope = interpreter.interpret(ast1, initialScope)
        assertEquals("\"Hello World\"", newScope.get("a"))
    }

    @Test
    fun testAssignationWithNumber() {
        val tokens = Lexer("let c : Number = 4; c = 2;")
        val parser = Parser(tokens)
        val ast1 = parser.next()
        val ast2 = parser.next()
        val initialScope = Environment()
        val newScope = interpreter.interpret(ast1, initialScope)
        val finalScope = interpreter.interpret(ast2, newScope)
        assertEquals(2, finalScope.get("c"))
    }

    @Test
    fun testAssignationWithLiteral() {
        val tokens = Lexer("let b: Number = 3; let a: Number = 7; a = b;")
        val parser = Parser(tokens)
        val ast1 = parser.next()
        val ast2 = parser.next()
        val ast3 = parser.next()
        val initialScope = Environment()
        val newScope = interpreter.interpret(ast1, initialScope)
        val intermediateScope = interpreter.interpret(ast2, newScope)
        val finalScope = interpreter.interpret(ast3, intermediateScope)
        assertEquals(3, finalScope.get("a"))
    }

    @Test
    fun testAssignationDeclarationWithNumber() {
        val tokens = Lexer("let a: Number = 2; a = 3;")
        val parser = Parser(tokens)
        val ast1 = parser.next()
        val ast2 = parser.next()
        val initialScope = Environment()
        val newScope = interpreter.interpret(ast1, initialScope)
        val finalScope = interpreter.interpret(ast2, newScope)
        assertEquals(3, finalScope.get("a"))
    }

    @Test
    fun testAssignationDeclarationWithString() {
        val tokens = Lexer("let b: String; b = 'Hello';")
        val parser = Parser(tokens)
        val ast1 = parser.next()
        val ast2 = parser.next()
        val initialScope = Environment()
        val newScope = interpreter.interpret(ast1, initialScope)
        val finalScope = interpreter.interpret(ast2, newScope)
        assertEquals("'Hello'", finalScope.get("b"))
    }

    @Test
    fun testMethodCallWithNumber() {
        val tokens = Lexer("let a: Number = 4; println(a);")
        val parser = Parser(tokens)
        val ast1 = parser.next()
        val ast2 = parser.next()
        val initialScope = Environment()
        val newScope = interpreter.interpret(ast1, initialScope)
        val finalScope = interpreter.interpret(ast2, newScope)
        assertEquals(4, finalScope.get("a"))
    }

    @Test
    fun testSumNumber() {
        val tokens = Lexer("let a: Number = 6 + 2 + 6;")
        val parser = Parser(tokens)
        val ast1 = parser.next()
        val initialScope = Environment()
        val newScope = interpreter.interpret(ast1, initialScope)
        assertEquals(14, newScope.get("a"))
    }

    @Test
    fun testDivisionNumber() {
        val tokens = Lexer("let a: Number = 6 / 2;")
        val parser = Parser(tokens)
        val ast1 = parser.next()
        val initialScope = Environment()
        val newScope = interpreter.interpret(ast1, initialScope)
        assertEquals(3, newScope.get("a"))
    }

    @Test
    fun testComplexExpression() {
        val tokens = Lexer("let a: Number = 6 / (2 + 5) - (5 * 6);")
        val parser = Parser(tokens)
        val ast1 = parser.next()
        val initialScope = Environment()
        val newScope = interpreter.interpret(ast1, initialScope)
        assertEquals(-30, newScope.get("a"))
    }

    @Test
    fun testSumWithIdentifier() {
        val tokens = Lexer("let a: Number = 6; let b: Number = a + 2;")
        val parser = Parser(tokens)
        val ast1 = parser.next()
        val ast2 = parser.next()
        val initialScope = Environment()
        val newScope = interpreter.interpret(ast1, initialScope)
        val finalScope = interpreter.interpret(ast2, newScope)
        assertEquals(8, finalScope.get("b"))
    }

    @Test
    fun testBinaryOperationString() {
        val tokens = Lexer("let a: String = 'Hello' + 'World';")
        val parser = Parser(tokens)
        val ast1 = parser.next()
        val initialScope = Environment()
        val newScope = interpreter.interpret(ast1, initialScope)
        assertEquals("'Hello''World'", newScope.get("a"))
    }

    @Test
    fun testAddingAssignations() {
        val tokens = Lexer("let a: Number = 7; let b : Number = 8; let c : Number = a + 3 + b;")
        val parser = Parser(tokens)
        val ast1 = parser.next()
        val ast2 = parser.next()
        val ast3 = parser.next()
        val initialScope = Environment()
        val newScope = interpreter.interpret(ast1, initialScope)
        val intermediateScope = interpreter.interpret(ast2, newScope)
        val finalScope = interpreter.interpret(ast3, intermediateScope)
        assertEquals(18, finalScope.get("c"))
    }
}
