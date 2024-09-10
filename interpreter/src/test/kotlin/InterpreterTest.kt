import interpreter.Interpreter
import lexer.Lexer
import org.junit.Assert.assertEquals
import org.junit.Test
import parser.Parser
class InterpreterTest {

    val version = "1.0"

    @Test
    fun testDeclarationWithNumber() {
        val tokens = Lexer("let a: Number;", version)
        val parser = Parser(tokens, version)
        val interpreter = Interpreter()
        val initialScope = Environment()
        val result = interpreter.interpret(initialScope, parser, StringBuilder())
        assertEquals(null, result.second.get("a"))
    }

    @Test
    fun testDeclarationWithString() {
        val tokens = Lexer("let a: String;", version)
        val parser = Parser(tokens, version)
        val interpreter = Interpreter()
        val initialScope = Environment()
        val result = interpreter.interpret(initialScope, parser, StringBuilder())
        assertEquals(null, result.second.get("a"))
    }

    @Test
    fun testAssignationWithString() {
        val tokens = Lexer("let a: String = \"Hello World\" ;", version)
        val parser = Parser(tokens, version)
        val interpreter = Interpreter()
        val initialScope = Environment()
        val result = interpreter.interpret(initialScope, parser, StringBuilder())
        assertEquals("\"Hello World\"", result.second.get("a"))
    }

    @Test
    fun testMethodCallWithNumber() {
        val tokens = Lexer("let a: Number = 4; println(a);", version)
        val parser = Parser(tokens, version)
        val interpreter = Interpreter()
        val initialScope = Environment()
        val stringBuilder = StringBuilder()
        val result = interpreter.interpret(initialScope, parser, stringBuilder)
        assertEquals(4, result.second.get("a"))
        assertEquals("\n4\n", result.first.toString()) // Verificar lo que se imprimió
    }

    @Test
    fun testSumNumber() {
        val tokens = Lexer("let a: Number = 6 + 2 + 6;", version)
        val parser = Parser(tokens, version)
        val interpreter = Interpreter()
        val initialScope = Environment()
        val result = interpreter.interpret(initialScope, parser, StringBuilder())
        assertEquals(14, result.second.get("a"))
    }

    @Test
    fun testBinaryOperationString() {
        val tokens = Lexer("let a: String = 'Hello' + 'World';", version)
        val parser = Parser(tokens, version)
        val interpreter = Interpreter()
        val initialScope = Environment()
        val result = interpreter.interpret(initialScope, parser, StringBuilder())
        assertEquals("'Hello''World'", result.second.get("a"))
    }

    @Test
    fun testAddingAssignations() {
        val tokens = Lexer("let a: Number = 7; let b : Number = 8; let c : Number = a + 3 + b;", version)
        val parser = Parser(tokens, version)
        val interpreter = Interpreter()
        val initialScope = Environment()
        val result = interpreter.interpret(initialScope, parser, StringBuilder())
        assertEquals(18, result.second.get("c"))
    }

    @Test
    fun testSinglePrint() {
        val tokens = Lexer("println(\"Hello, World!\");", version)
        val parser = Parser(tokens, version)
        val interpreter = Interpreter()
        val initialScope = Environment()
        val stringBuilder = StringBuilder()
        val result = interpreter.interpret(initialScope, parser, stringBuilder)

        // Verificamos que el StringBuilder haya guardado el contenido impreso
        assertEquals("\n\"Hello, World!\"\n", result.first.toString())
    }

    @Test
    fun testMultiplePrintStatements() {
        val tokens = Lexer("println(\"First print\"); println(\"Second print\");", version)
        val parser = Parser(tokens, version)
        val interpreter = Interpreter()
        val initialScope = Environment()
        val stringBuilder = StringBuilder()
        val result = interpreter.interpret(initialScope, parser, stringBuilder)

        // Verificamos que ambos prints estén guardados en el StringBuilder
        assertEquals("\n\"First print\"\n\n\"Second print\"\n", result.first.toString())
    }

    @Test
    fun testPrintExpressionAndVariable() {
        val tokens = Lexer(
            "let a: Number = 42; println(a); println(a + 8);",
            version
        )
        val parser = Parser(tokens, version)
        val interpreter = Interpreter()
        val initialScope = Environment()
        val stringBuilder = StringBuilder()
        val result = interpreter.interpret(initialScope, parser, stringBuilder)

        // Verificamos que se haya impreso tanto la variable como la expresión
        assertEquals("\n42\n\n50\n", result.first.toString())
        assertEquals(42, result.second.get("a")) // Verificamos el valor de la variable
    }

    private val interpreter = Interpreter()

    @Test
    fun testDivisionNumber() {
        val tokens = Lexer("let a: Number = 6 / 2;", version)
        val parser = Parser(tokens, version)
        val initialScope = Environment()
        val stringBuilder = StringBuilder()

        val finalScope = interpreter.interpret(initialScope, parser, stringBuilder)
        assertEquals(3, finalScope.second.get("a"))
        // No hay salida esperada en el StringBuilder para esta operación
        assertEquals("", finalScope.first.toString())
    }

    @Test
    fun testComplexExpression() {
        val tokens = Lexer("let a: Number = 6 / (2 + 5) - (5 * 6);", version)
        val parser = Parser(tokens, version)
        val initialScope = Environment()
        val stringBuilder = StringBuilder()

        val finalScope = interpreter.interpret(initialScope, parser, stringBuilder)
        assertEquals(-30, finalScope.second.get("a"))
        // No hay salida esperada en el StringBuilder para esta operación
        assertEquals("", finalScope.first.toString())
    }

    @Test
    fun testSumWithIdentifier() {
        val tokens = Lexer("let a: Number = 6; let b: Number = a + 2;", version)
        val parser = Parser(tokens, version)

        val initialScope = Environment()
        val stringBuilder = StringBuilder()
        val intermediateScope = interpreter.interpret(initialScope, parser, stringBuilder)
        val finalScope = interpreter.interpret(intermediateScope.second, parser, stringBuilder)
        assertEquals(8, finalScope.second.get("b"))
        // No hay salida esperada en el StringBuilder para esta operación
        assertEquals("", finalScope.first.toString())
    }
}
