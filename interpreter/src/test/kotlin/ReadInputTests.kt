import exception.SemanticErrorException
import interpreter.Interpreter
import lexer.Lexer
import nodes.Expression
import nodes.StatementType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import parser.Parser
import position.Position
import position.visitor.InputProvider
import position.visitor.PrintScriptInputProvider

class ReadInputTests {
    val version = "1.1"


    @Test
    fun testReadInput() {
        val fileContents = "const name: string = readInput(\"Name:\"); println(\"Hello \" + name + \"!\");"
        val input = "world"

        val tokens = Lexer(fileContents, version)

        val inputProvider = PrintScriptInputProvider(mapOf("Name:" to input))
        val asts = Parser(tokens, version, inputProvider)
        var outputBuilder = StringBuilder()
        var currentEnvironment = createEnvironmentFromMap(System.getenv())

        val ast1 = asts.next()
        val ast2 = asts.next()



        val result1 = Interpreter.interpret(ast1, version, currentEnvironment, outputBuilder, inputProvider)
        currentEnvironment = result1.second
        outputBuilder = result1.first
        val result2 = Interpreter.interpret(ast2, version, currentEnvironment, outputBuilder, inputProvider)
        currentEnvironment = result2.second
        outputBuilder = result2.first

        assertEquals("Hello world!", outputBuilder.toString())
    }

    @Test
    fun testMultipleReadInputWithConcatenation() {
        val fileContents = """
        const firstName: string = readInput("First Name:");
        const lastName: string = readInput("Last Name:");
        println("Welcome " + firstName + " " + lastName + "!");
    """.trimIndent()
        val inputs = mapOf("First Name:" to "John", "Last Name:" to "Doe")

        val tokens = Lexer(fileContents, version)
        val inputProvider = PrintScriptInputProvider(inputs)
        val asts = Parser(tokens, version, inputProvider)

        var outputBuilder = StringBuilder()
        var currentEnvironment = createEnvironmentFromMap(System.getenv())

        while (asts.hasNext()) {
            val ast = asts.next()
            val result = Interpreter.interpret(ast, version, currentEnvironment, outputBuilder, inputProvider)
            currentEnvironment = result.second
            outputBuilder = result.first
        }

        assertEquals("Welcome John Doe!", outputBuilder.toString())
    }


    @Test
    fun testReadInputTypeMismatch() {
        val fileContents = "const age: number = readInput(\"Age:\"); println(age);"
        val input = "notANumber"

        val tokens = Lexer(fileContents, version)
        val inputProvider = PrintScriptInputProvider(mapOf("Age:" to input))
        val asts = Parser(tokens, version, inputProvider)

        var outputBuilder = StringBuilder()
        var currentEnvironment = createEnvironmentFromMap(System.getenv())



        val exception = assertThrows(SemanticErrorException::class.java) {
            val ast = asts.next()
            Interpreter.interpret(ast, version, currentEnvironment, outputBuilder, inputProvider)
        }
        println(exception)
    }

    @Test
    fun testReadInputInsideIfStatement() {
        val fileContents = """
        if (readInput("Do you want to proceed?")) {
            println("Proceeding...");
        } else {
            println("Cancelled");
        }
    """.trimIndent()
        val input = "true"

        val tokens = Lexer(fileContents, version)
        val inputProvider = PrintScriptInputProvider(mapOf("Do you want to proceed?" to input))
        val asts = Parser(tokens, version, inputProvider)

        var outputBuilder = StringBuilder()
        var currentEnvironment = createEnvironmentFromMap(System.getenv())

        while (asts.hasNext()) {
            val ast = asts.next()
            val result = Interpreter.interpret(ast, version, currentEnvironment, outputBuilder, inputProvider)
            currentEnvironment = result.second
            outputBuilder = result.first
        }

        assertEquals("Proceeding...", outputBuilder.toString())
    }

    private fun createEnvironmentFromMap(envVarsMap: Map<String, String>): Environment {
        var env = Environment()

        for ((key, value) in envVarsMap) {
            val variable = StatementType.Variable(
                designation = "const",
                identifier = key,
                initializer = Expression.Literal(value, Position(0, 0)),
                dataType = "string",
                position = Position(0, 0)
            )

            env = env.define(variable)
        }

        return env
    }
}