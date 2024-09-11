import interpreter.Interpreter
import lexer.Lexer
import org.junit.Assert.assertEquals
import org.junit.Test
import parser.Parser

class InterpreterTest {

    val version = "1.0"

    @Test
    fun testDeclarationWithNumber() {
        val input = "let a: number;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val result = Interpreter.interpret(asts, version)
        assertEquals(null, result.second.get("a").initializer)
    }

    @Test
    fun testDeclarationWithString() {
        val input = "let a: string;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val result = Interpreter.interpret(asts, version)
        assertEquals(null, result.second.get("a").initializer)
    }

    @Test
    fun testAssignationWithString() {
        val input = "let a: string = \"Hello World\" ;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val result = Interpreter.interpret(asts, version)
        assertEquals("\"Hello World\"", result.second.get("a").initializer?.value)
    }

    @Test
    fun testMethodCallWithNumber() {
        val input = "let a: number = 4; println(a);"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val result = Interpreter.interpret(asts, version)
        assertEquals(4, result.second.get("a").initializer)
        assertEquals("\n4\n", result.first.toString()) // Verificar lo que se imprimió
    }

    @Test
    fun testSumNumber() {
        val input = "let a: number = 6 + 2 + 6;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val result = Interpreter.interpret(asts, version)
        assertEquals(14, result.second.get("a").initializer?.value)
    }

    @Test
    fun testBinaryOperationString() {
        val input = "let a: string = 'Hello' + 'World';"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val result = Interpreter.interpret(asts, version)
        assertEquals("'Hello''World'", result.second.get("a").initializer?.value)
    }

    @Test
    fun testAddingAssignations() {
        val input = "let a: number = 7; let b : number = 8; let c : number = a + 3 + b;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val result = Interpreter.interpret(asts, version)
        assertEquals(18, result.second.get("c").initializer?.value)
    }

    @Test
    fun testSinglePrint() {
        val input = "println(\"Hello, World!\");"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val result = Interpreter.interpret(asts, version)

        // Verificamos que el StringBuilder haya guardado el contenido impreso
        assertEquals("\n\"Hello, World!\"\n", result.first.toString())
    }

    @Test
    fun testMultiplePrintStatements() {
        val input = "println(\"First print\"); println(\"Second print\");"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val result = Interpreter.interpret(asts, version)

        // Verificamos que ambos prints estén guardados en el StringBuilder
        assertEquals("\n\"First print\"\n\n\"Second print\"\n", result.first.toString())
    }

    @Test
    fun testPrintExpressionAndVariable() {
        val input = "let a: number = 42; println(a); println(a + 8);"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val result = Interpreter.interpret(asts, version)

        // Verificamos que se haya impreso tanto la variable como la expresión
        assertEquals("\n42\n\n50\n", result.first.toString())
        assertEquals(42, result.second.get("a").initializer?.value) // Verificamos el valor de la variable
    }

    @Test
    fun testDivisionNumber() {
        val input = "let a: number = 6 / 2;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val result = Interpreter.interpret(asts, version)
        // No hay salida esperada en el StringBuilder para esta operación
        assertEquals("", result.first.toString())
    }

    @Test
    fun testComplexExpression() {
        val input = "let a: number = 6 / (2 + 5) - (5 * 6);"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val result = Interpreter.interpret(asts, version)
        // No hay salida esperada en el StringBuilder para esta operación
        assertEquals("", result.first.toString())
    }

    @Test
    fun testSumWithIdentifier() {
        val input = "let a: number = 6; let b: number = a + 2;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val result = Interpreter.interpret(asts, version)
        assertEquals(8, result.second.get("b").initializer?.value)
        // No hay salida esperada en el StringBuilder para esta operación
        assertEquals("", result.first.toString())
    }
}
