import interpreter.Interpreter
import lexer.Lexer
import org.junit.Assert.assertEquals
import org.junit.Test
import parser.Parser
import position.visitor.Environment

class InterpreterTest {

    val version = "1.0"

    @Test
    fun testDeclarationWithNumber() {
        val input = "let a: number;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }
    }

    @Test
    fun testDeclarationWithString() {
        val input = "let a: string;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }
    }

    @Test
    fun testAssignationWithString() {
        val input = "let a: string = \"Hello World\" ;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }
    }

    @Test
    fun testMethodCallWithNumber() {
        val input = "let a: number = 4; println(a);"
        val version = "1.1"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)

        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        val variable = currentEnvironment.get("a")
        val initializer = variable.initializer
        val actual = initializer?.value

        assertEquals(4, actual)
        assertEquals("\n4\n", outputBuilder.toString())
    }

    @Test
    fun testSumNumber() {
        val input = "let a: number = 6 + 2 + 6;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }
    }

    @Test
    fun testBinaryOperationString() {
        val input = "let a: string = 'Hello' + 'World';"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }
    }

    @Test
    fun testAddingAssignations() {
        val input = "let a: number = 7; let b : number = 8; let c : number = a + 3 + b;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }
    }

    @Test
    fun testSinglePrint() {
        val input = "println(\"Hello, World!\");"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }
    }

    @Test
    fun testMultiplePrintStatements() {
        val input = "println(\"First print\"); println(\"Second print\");"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        // Verificamos que ambos prints estén guardados en el StringBuilder
    }

    @Test
    fun testPrintExpressionAndVariable() {
        val input = "let a: number = 42; println(a); println(a + 8);"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }
    }

    @Test
    fun testDivisionNumber() {
        val input = "let a: number = 6 / 2;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }
    }

    @Test
    fun testComplexExpression() {
        val input = "let a: number = 6 / (2 + 5) - (5 * 6);"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }
    }

    @Test
    fun testSumWithIdentifier() {
        val input = "let a: number = 6; let b: number = a + 2;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }
    }
}
