import interpreter.Interpreter
import lexer.Lexer
import org.junit.Assert.assertEquals
import org.junit.Test
import parser.Parser
import position.visitor.Environment

class InterpreterTest {

    val version = "1.1"

    @Test
    fun testDeclarationWithNumber() {
        val input = "let a: number;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)

        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        // Check that the variable 'a' was declared, but has no value
        val variable = currentEnvironment.get("a")
        assertEquals(null, variable.initializer?.value)
    }

    @Test
    fun testPrintOperation() {
        val input = "let numberResult: number = 5 * 5 - 8; println(numberResult);"
        val tokens = Lexer(input, "1.0")
        val asts = Parser(tokens, "1.0")

        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        assertEquals("17", outputBuilder.toString().trim())
    }

    @Test
    fun testStringAndNumberConcat() {
        val input = "let someNumber: number = 1; let someString: string = \"hello world \";\n println(someString + someNumber);\n"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)

        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        assertEquals("hello world 1", outputBuilder.toString().trim())
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
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        // Check that the variable 'a' was declared, but has no value
        val variable = currentEnvironment.get("a")
        assertEquals(null, variable.initializer?.value)
    }

    @Test
    fun testAssignationWithString() {
        val input = "let a: string = \"Hello World\";"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)

        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        // Check that the variable 'a' has the expected value
        val variable = currentEnvironment.get("a")
        val initializer = variable.initializer
        val actual = initializer?.value

        // assertEquals("Hello World", actual)
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
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        // Check that the variable 'a' has the expected value
        val variable = currentEnvironment.get("a")
        val initializer = variable.initializer
        val actual = initializer?.value

        assertEquals(14, actual)
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
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        // Check that the variable 'a' has the expected concatenated value
        val variable = currentEnvironment.get("a")
        val initializer = variable.initializer
        val actual = initializer?.value

        // assertEquals('\Hello''World', actual)
    }

    @Test
    fun testAddingAssignations() {
        val input = "let a: number = 7; let b: number = 8; let c: number = a + 3 + b;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)

        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        // Check that the variable 'c' has the expected value
        val variable = currentEnvironment.get("c")
        val initializer = variable.initializer
        val actual = initializer?.value

        assertEquals(18, actual)
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
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        // Check that the output matches the expected print result
        assertEquals("Hello, World!", outputBuilder.toString())
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
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        // Verify that both print statements are present in the output
        assertEquals("First print\nSecond print", outputBuilder.toString())
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
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        // Verify the output for the variable and the expression
        assertEquals("\n42\n50\n", outputBuilder.toString())
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
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        // Check that the variable 'a' has the expected value
        val variable = currentEnvironment.get("a")
        val initializer = variable.initializer
        val actual = initializer?.value

        assertEquals(3, actual)
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
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        // Check that the variable 'a' has the expected value
        val variable = currentEnvironment.get("a")
        val initializer = variable.initializer
        val actual = initializer?.value

        assertEquals(-30, actual)
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
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        // Check that the variable 'b' has the expected value
        val variable = currentEnvironment.get("b")
        val initializer = variable.initializer
        val actual = initializer?.value

        assertEquals(8, actual)
    }

    @Test
    fun testValidConstDeclaration() {
        val input = "const b: string = \"this should be valid in 1.1\";"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)

        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }
    }

    @Test
    fun testValidLetDeclaration() {
        val input = "let b: string = \"this should be valid in 1.1\";"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)

        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }
    }

    @Test
    fun testIfElse() {
        val input = """
    const booleanValue: boolean = false;
    if(booleanValue) {
        println("if statement is not working correctly");
    }
    println("outside of conditional");
        """.trimIndent()

        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)

        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        assertEquals("outside of conditional", outputBuilder.toString().trim())
    }

    @Test
    fun testReadInput() {
        val file = "const name: string = readInput(\"Name:\"); println(\"Hello \" + name + \"!\");"
        val input = "world"

        val tokens = Lexer(file, version)
        val asts = Parser(tokens, version, input)

        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version, currentEnvironment, input)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        assertEquals("Name:\n" + "Hello world!", outputBuilder.toString().trim())
    }

    @Test
    fun print() {
        val input = "println(1 + 1 + 1);"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)

        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }

        assertEquals("3", outputBuilder.toString())
    }

    @Test
    fun testIncrementCoverage() {
        val input = "let x: number;"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)

        val outputBuilder = StringBuilder()
        var currentEnvironment = Environment()

        while (asts.hasNext()) {
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder.append(result.first.toString())
            currentEnvironment = result.second
        }
    }
}
