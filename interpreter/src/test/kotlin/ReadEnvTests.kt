import environment.Environment
import interpreter.Interpreter
import lexer.Lexer
import nodes.Expression
import nodes.Statement
import org.junit.Assert.assertEquals
import org.junit.Test
import parser.Parser
import token.Position

class ReadEnvTests {

  private val version = "1.1"

  @Test
  fun testReadEnvHelloWorld() {
    val input =
      "const name: string = readEnv(\"NAME\"); " +
        "println(\"Hello \" + name + 22);\n"

    val tokens = Lexer(input, version)
    val asts = Parser(tokens, version)

    var outputBuilder = StringBuilder()
    var currentEnvironment = Environment().define(createVariable("NAME", "WORLD"))

    while (asts.hasNext()) {
      asts.setEnv(currentEnvironment)
      val statement = asts.next()
      val result = Interpreter.interpret(statement, version, currentEnvironment)
      outputBuilder = result.first
      currentEnvironment = result.second
      asts.setEnv(currentEnvironment)
    }

    assertEquals("Hello WORLD22", outputBuilder.toString().trim())
  }

  @Test
  fun testReadEnvMultipleVariables() {
    val input =
      "const firstName: string = readEnv(\"FIRST_NAME\"); " +
        "const lastName: string = readEnv(\"LAST_NAME\"); " +
        "println(\"Hello \" + firstName + \" \" + lastName);\n"

    val tokens = Lexer(input, version)
    val asts = Parser(tokens, version)

    var outputBuilder = StringBuilder()

    var currentEnvironment = Environment()
      .define(createVariable("FIRST_NAME", "John"))
      .define(createVariable("LAST_NAME", "Doe"))

    while (asts.hasNext()) {
      asts.setEnv(currentEnvironment)
      val statement = asts.next()
      val result = Interpreter.interpret(statement, version, currentEnvironment)
      outputBuilder = result.first
      currentEnvironment = result.second
      asts.setEnv(currentEnvironment)
    }

    assertEquals("Hello John Doe", outputBuilder.toString().trim())
  }

  @Test
  fun testReadEnvUndefinedVariable() {
    val input =
      "const age: number = readEnv(\"AGE\"); " +
        "println(age);\n"

    val tokens = Lexer(input, version)
    val asts = Parser(tokens, version)

    var outputBuilder = StringBuilder()

    var currentEnvironment = Environment().define(createVariable("AGE", "4"))

    while (asts.hasNext()) {
      asts.setEnv(currentEnvironment)
      val statement = asts.next()
      val result = Interpreter.interpret(statement, version, currentEnvironment)
      outputBuilder = result.first
      currentEnvironment = result.second
      asts.setEnv(currentEnvironment)
    }

    assertEquals("4", outputBuilder.toString().trim())
  }

  @Test
  fun testReadEnvTypeMismatch() {
    val input =
      "let a: number = readEnv(\"NUMERO\"); " +
        "println(a);\n"

    val tokens = Lexer(input, version)
    val asts = Parser(tokens, version)

    var outputBuilder = StringBuilder()

    var currentEnvironment = Environment().define(createVariable("NUMERO", "hola"))

    try {
      while (asts.hasNext()) {
        asts.setEnv(currentEnvironment)
        val statement = asts.next()
        val result = Interpreter.interpret(statement, version, currentEnvironment)
        outputBuilder = result.first
        currentEnvironment = result.second
        asts.setEnv(currentEnvironment)
      }
    } catch (e: IllegalArgumentException) {
      assertEquals("Expected an Integer but got: hola at Line 1, symbol 23", e.message)
    }
  }

  fun createVariable(name: String, value: String): Statement.Variable {
    return Statement.Variable(
      designation = "const",
      identifier = name,
      initializer = Expression.Literal(value, Position(1, 1)), // Literal con valor dinámico
      dataType = "string",
      position = Position(1, 1)
    )
  }
}
