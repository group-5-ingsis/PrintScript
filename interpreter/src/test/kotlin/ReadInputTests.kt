
import environment.Environment
import interpreter.Interpreter
import lexer.Lexer
import nodes.Expression
import nodes.StatementType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import parser.Parser
import token.Position
import visitor.PrintScriptInputProvider

class ReadInputTests {
  private val version = "1.1"

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

    val result1 = Interpreter.interpret(ast1, version, currentEnvironment, inputProvider)
    currentEnvironment = result1.second
    outputBuilder = result1.first
    val result2 = Interpreter.interpret(ast2, version, currentEnvironment, inputProvider)
    currentEnvironment = result2.second
    outputBuilder = result2.first

    assertEquals("Hello world!\n", outputBuilder.toString())
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
      val result = Interpreter.interpret(ast, version, currentEnvironment, inputProvider)
      currentEnvironment = result.second
      outputBuilder = result.first
    }

    assertEquals("Welcome John Doe!\n", outputBuilder.toString())
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

    val exception = assertThrows(IllegalArgumentException::class.java) {
      val ast = asts.next()
      Interpreter.interpret(ast, version, currentEnvironment, inputProvider)
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
      val result = Interpreter.interpret(ast, version, currentEnvironment, inputProvider)
      currentEnvironment = result.second
      outputBuilder = result.first
    }

    assertEquals("Proceeding...\n", outputBuilder.toString())
  }

  @Test
  fun testInvalidAssignmentWithReadInput() {
    val fileContents = "const value: number = readInput(\"Enter a string:\");"
    val input = "hello"

    val tokens = Lexer(fileContents, version)
    val inputProvider = PrintScriptInputProvider(mapOf("Enter a string:" to input))
    val asts = Parser(tokens, version, inputProvider)

    var outputBuilder = StringBuilder()
    var currentEnvironment = createEnvironmentFromMap(System.getenv())

    val exception = assertThrows(IllegalArgumentException::class.java) {
      val ast = asts.next()
      Interpreter.interpret(ast, version, currentEnvironment, inputProvider)
    }
  }

  @Test
  fun testReadInputWithDifferentDataTypes() {
    val fileContents = """
        const stringVal: string = readInput("Enter string:");
        const numberVal: number = readInput("Enter number:");
        const booleanVal: string = readInput("Enter string again:");
        println(stringVal + "" + numberVal + " and " + booleanVal);
    """.trimIndent()
    val inputs = mapOf(
      "Enter string:" to "The next should be a number and a string: ",
      "Enter number:" to "42",
      "Enter string again:" to "true"
    )

    val tokens = Lexer(fileContents, version)
    val inputProvider = PrintScriptInputProvider(inputs)
    val asts = Parser(tokens, version, inputProvider)

    var outputBuilder = StringBuilder()
    var currentEnvironment = createEnvironmentFromMap(System.getenv())

    while (asts.hasNext()) {
      val ast = asts.next()
      val result = Interpreter.interpret(ast, version, currentEnvironment, inputProvider)
      currentEnvironment = result.second
      outputBuilder = result.first
    }

    assertEquals("The next should be a number and a string: 42 and true\n", outputBuilder.toString())
  }

  @Test
  fun testReadInputWithVeryLongInput() {
    val fileContents = "const longString: string = readInput(\"Long input:\"); println(longString);"
    val input = "a".repeat(1000000) // 1 million characters

    val tokens = Lexer(fileContents, version)
    val inputProvider = PrintScriptInputProvider(mapOf("Long input:" to input))
    val asts = Parser(tokens, version, inputProvider)

    var outputBuilder = StringBuilder()
    var currentEnvironment = createEnvironmentFromMap(System.getenv())

    while (asts.hasNext()) {
      val ast = asts.next()
      val result = Interpreter.interpret(ast, version, currentEnvironment, inputProvider)
      currentEnvironment = result.second
      outputBuilder = result.first
    }

    assertEquals(input + "\n", outputBuilder.toString())
  }

  @Test
  fun testReadInputWithInlineMathOperations() {
    val fileContents = """
        const result: number = readInput("Enter the first number:") + readInput("Enter the second number:");
        println("Result: " + result);
    """.trimIndent()

    val inputs = mapOf(
      "Enter the first number:" to "10",
      "Enter the second number:" to "5"
    )

    val tokens = Lexer(fileContents, version)
    val inputProvider = PrintScriptInputProvider(inputs)
    val asts = Parser(tokens, version, inputProvider)

    var outputBuilder = StringBuilder()
    var currentEnvironment = createEnvironmentFromMap(System.getenv())

    while (asts.hasNext()) {
      val ast = asts.next()
      val result = Interpreter.interpret(ast, version, currentEnvironment, inputProvider)
      currentEnvironment = result.second
      outputBuilder = result.first
    }

    val expectedOutput = "Result: 15" // Resultado esperado de la operación encadenada

    assertEquals(expectedOutput, outputBuilder.toString().trim())
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
