import environment.Environment
import interpreter.Interpreter
import lexer.Lexer
import nodes.Expression
import nodes.StatementType
import org.junit.Assert.assertEquals
import org.junit.Test
import parser.Parser
import token.Position

class InterpreterTest {

  private val version = "1.1"

  @Test
  fun testDeclarationWithNumber() {
    val input = "let a: number;"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)

    val outputBuilder = StringBuilder()
    var currentEnvironment = Environment()

    while (asts.hasNext()) {
      val statement = asts.next()
      val result = Interpreter.interpret(statement, version, currentEnvironment)
      outputBuilder.append(result.first.toString())
      currentEnvironment = result.second
    }

    val variable = currentEnvironment.getValue("a")
    assertEquals(null, variable)
  }

  @Test
  fun testPrintOperation() {
    val input = "let numberResult: number = 5 * 5 - 8; println(numberResult);"
    val tokens = Lexer.fromString(input, "1.0")
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
    val tokens = Lexer.fromString(input, version)
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
    val tokens = Lexer.fromString(input, version)
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
    val variable = currentEnvironment.getValue("a")
    assertEquals(null, variable)
  }

  @Test
  fun testAssignationWithString() {
    val input = "let a: string = \"Hello World\";"
    val tokens = Lexer.fromString(input, version)
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
    val variable = currentEnvironment.getValue("a")

    assertEquals("Hello World", variable)
  }

  @Test
  fun testSumNumber() {
    val input = "let a: number = 6 + 2 + 6;"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)

    val outputBuilder = StringBuilder()
    var currentEnvironment = Environment()

    while (asts.hasNext()) {
      val statement = asts.next()
      val result = Interpreter.interpret(statement, version, currentEnvironment)
      outputBuilder.append(result.first.toString())
      currentEnvironment = result.second
    }

    val variable = currentEnvironment.getValue("a")

    assertEquals(14, variable)
  }

  @Test
  fun testBinaryOperationString() {
    val input = "let a: string = 'Hello' + ' World';"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)

    val outputBuilder = StringBuilder()
    var currentEnvironment = Environment()

    while (asts.hasNext()) {
      val statement = asts.next()
      val result = Interpreter.interpret(statement, version, currentEnvironment)
      outputBuilder.append(result.first.toString())
      currentEnvironment = result.second
    }

    val variable = currentEnvironment.getValue("a")

    assertEquals("Hello World", variable)
  }

  @Test
  fun testAddingAssignations() {
    val input = "let a: number = 7; let b: number = 8; let c: number = a + 3 + b;"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)

    val outputBuilder = StringBuilder()
    var currentEnvironment = Environment()

    while (asts.hasNext()) {
      val statement = asts.next()
      val result = Interpreter.interpret(statement, version, currentEnvironment)
      outputBuilder.append(result.first.toString())
      currentEnvironment = result.second
    }

    val variable = currentEnvironment.getValue("c")

    assertEquals(18, variable)
  }

  @Test
  fun testSinglePrint() {
    val input = "println(\"Hello, World!\");"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)

    val outputBuilder = StringBuilder()
    var currentEnvironment = Environment()

    while (asts.hasNext()) {
      val statement = asts.next()
      val result = Interpreter.interpret(statement, version, currentEnvironment)
      outputBuilder.append(result.first.toString())
      currentEnvironment = result.second
    }

    assertEquals("Hello, World!\n", outputBuilder.toString())
  }

  @Test
  fun testMultiplePrintStatements() {
    val fileContents = "println(\"First print\"); println(\"Second print\");"
    val tokens = Lexer.fromString(fileContents, version)
    val asts = Parser(tokens, version)

    var outputBuilder = StringBuilder()
    var currentEnvironment = createEnvironmentFromMap(System.getenv())

    while (asts.hasNext()) {
      asts.setEnv(currentEnvironment)
      val statement = asts.next()
      val result = Interpreter.interpret(statement, version, currentEnvironment)
      outputBuilder.append(result.first)
      currentEnvironment = result.second
      asts.setEnv(currentEnvironment)
    }

    assertEquals("First print\nSecond print\n", outputBuilder.toString())
  }

  @Test
  fun testPrintExpressionAndVariable() {
    val input = "let a: number = 42; println(a); println(a + 8);"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)

    var outputBuilder = StringBuilder()
    var currentEnvironment = createEnvironmentFromMap(System.getenv())

    while (asts.hasNext()) {
      asts.setEnv(currentEnvironment)
      val statement = asts.next()
      val result = Interpreter.interpret(statement, version, currentEnvironment)
      outputBuilder.append(result.first)
      currentEnvironment = result.second
      asts.setEnv(currentEnvironment)
    }
    // Verify the output for the variable and the expression
    assertEquals("42\n50\n", outputBuilder.toString())
  }

  @Test
  fun testDivisionNumber() {
    val input = "let a: number = 6 / 2;"
    val tokens = Lexer.fromString(input, version)
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
    val variable = currentEnvironment.getValue("a")

    assertEquals(3, variable)
  }

  @Test
  fun testComplexExpression() {
    val input = "let a: number = 6 / (2 + 5) - 5 * 6;"
    val tokens = Lexer.fromString(input, version)
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
    val variable = currentEnvironment.getValue("a")

    assertEquals(-30, variable)
  }

  @Test
  fun testSumWithIdentifier() {
    val input = "let a: number = 6; let b: number = a + 2;"
    val tokens = Lexer.fromString(input, version)
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
    val variable = currentEnvironment.getValue("b")

    assertEquals(8, variable)
  }

  @Test
  fun testValidConstDeclaration() {
    val input = "const b: string = \"this should be valid in 1.1\";"
    val tokens = Lexer.fromString(input, version)
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
    val tokens = Lexer.fromString(input, version)
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

    val tokens = Lexer.fromString(input, version)
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

  @Test
  fun testReadEnv() {
    val input =
      "const name: string = readEnv(\"BEST_FOOTBALL_CLUB\"); " +
        "println(\"What is the best football club?\"); " +
        "println(name);\n"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)

    var currentEnvironment = createEnvironmentFromMap(System.getenv())
    val builder = StringBuilder()
    while (asts.hasNext()) {
      asts.setEnv(currentEnvironment)
      val statement = asts.next()
      val result = Interpreter.interpret(statement, version, currentEnvironment)
      builder.append(result.first.toString())
      currentEnvironment = result.second
      asts.setEnv(currentEnvironment)
    }

    assertEquals("What is the best football club?\n" + "San Lorenzo\n", builder.toString())
  }

  @Test
  fun print() {
    val input = "println(1 + 1 + 1);"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)

    val outputBuilder = StringBuilder()
    var currentEnvironment = Environment()

    while (asts.hasNext()) {
      val statement = asts.next()
      val result = Interpreter.interpret(statement, version, currentEnvironment)
      outputBuilder.append(result.first.toString())
      currentEnvironment = result.second
      asts.setEnv(currentEnvironment)
    }

    assertEquals("3\n", outputBuilder.toString())
  }

  @Test
  fun testIncrementCoverage() {
    val input = "let x: number;"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)

    val outputBuilder = StringBuilder()
    var currentEnvironment = Environment()

    while (asts.hasNext()) {
      val statement = asts.next()
      val result = Interpreter.interpret(statement, version, currentEnvironment)
      outputBuilder.append(result.first.toString())
      currentEnvironment = result.second
    }

    assertEquals(null, currentEnvironment.getValue("x"))
  }

  @Test
  fun testPrinter() {
    val input = "let a : number = 1; println(a);" // Si vos le agregas un espacio al final del string, el hasNext() del lexer tira -> True cuando deberia ser false
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)

    var outputBuilder = StringBuilder()
    var currentEnvironment = createEnvironmentFromMap(System.getenv())

    while (asts.hasNext()) {
      asts.setEnv(currentEnvironment)
      val statement = asts.next()
      val result = Interpreter.interpret(statement, version, currentEnvironment)
      outputBuilder = result.first
      currentEnvironment = result.second
      asts.setEnv(currentEnvironment)
    }
    assertEquals("1\n", outputBuilder.toString())
    assertEquals(1, currentEnvironment.getValue("a"))
  }
}
