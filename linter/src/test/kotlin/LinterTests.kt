package linter

import environment.EnvironmentCreator
import lexer.Lexer
import parser.Parser
import rules.LinterRules
import java.io.ByteArrayInputStream
import kotlin.test.Test
import kotlin.test.assertEquals

class LinterTests {

  @Test
  fun testPrintlnCallInvalid() {
    val input = "let a : string = 'hello'; " +
      "let b : string = a + 'world';" +
      "let n: number;" +
      "n = 9;" +
      "println(6 + 6);"

    val version = "1.0"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)
    val rules = LinterRules(version, "camel-case", false)

    var errorList: List<LinterResult> = emptyList()

    var currentEnv = EnvironmentCreator.create(System.getenv())

    while (asts.hasNext()) {
      currentEnv = asts.setEnv(currentEnv)
      val statement = asts.next()
      val lintResult = Linter.lint(statement, rules, version)

      if (!lintResult.isValid()) {
        errorList = errorList + lintResult
      }

      currentEnv = asts.getEnv()
    }

    val numOfErrors = errorList.size
    assertEquals(1, numOfErrors)
    assertEquals(
      "Errors found:\n" + "println() statements must receive a literal or identifier, not an expression. At Line 1, symbol 82, got BINARY_EXPRESSION.",
      errorList[0].message
    )
  }

  @Test
  fun testPrintlnCallValid() {
    val input = "println(6 + 6);"
    val version = "1.0"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)
    val rules = LinterRules(version, "camel-case", true)

    var errorList: List<LinterResult> = emptyList()

    var currentEnv = EnvironmentCreator.create(System.getenv())

    while (asts.hasNext()) {
      currentEnv = asts.setEnv(currentEnv)
      val statement = asts.next()
      val lintResult = Linter.lint(statement, rules, version)

      if (!lintResult.isValid()) {
        errorList = errorList + lintResult
      }

      currentEnv = asts.getEnv()
    }

    assertEquals(0, errorList.size)
  }

  @Test
  fun testPrintlnCallValidWithIdentifier() {
    val input = "let a : string = 'hello'; println(a);"
    val version = "1.0"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)
    val rules = LinterRules(version, "camel-case", false)

    var errorList: List<LinterResult> = emptyList()

    var currentEnv = EnvironmentCreator.create(System.getenv())

    while (asts.hasNext()) {
      currentEnv = asts.setEnv(currentEnv)
      val statement = asts.next()
      val lintResult = Linter.lint(statement, rules, version)

      if (!lintResult.isValid()) {
        errorList = errorList + lintResult
      }

      currentEnv = asts.getEnv()
    }

    assertEquals(0, errorList.size)
  }

  @Test
  fun testDeclarationOffValid() {
    val input = "let aA : string = 'hello'; let a_b : string = 'hello';"
    val version = "1.0"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)
    val rules = LinterRules(version, "off", false)

    var errorList: List<LinterResult> = emptyList()

    var currentEnv = EnvironmentCreator.create(System.getenv())

    while (asts.hasNext()) {
      currentEnv = asts.setEnv(currentEnv)
      val statement = asts.next()
      val lintResult = Linter.lint(statement, rules, version)

      if (!lintResult.isValid()) {
        errorList = errorList + lintResult
      }

      currentEnv = asts.getEnv()
    }

    assertEquals(0, errorList.size)
  }

  @Test
  fun testDeclarationSnake_CaseInvalid() {
    val input = "let aA : string = 'hello'; let a_b : string = 'hello';"
    val version = "1.0"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)
    val rules = LinterRules(version, "snake-case", false)

    var errorList: List<LinterResult> = emptyList()

    var currentEnv = EnvironmentCreator.create(System.getenv())

    while (asts.hasNext()) {
      currentEnv = asts.setEnv(currentEnv)
      val statement = asts.next()
      val lintResult = Linter.lint(statement, rules, version)

      if (!lintResult.isValid()) {
        errorList = errorList + lintResult
      }

      currentEnv = asts.getEnv()
    }

    assertEquals(1, errorList.size)
    assertEquals(
      "Errors found:\n" + "Variable names must be in snake_case at Line 1, symbol 5, got aA.",
      errorList[0].message
    )
  }

  @Test
  fun testDeclarationSnake_CaseValid() {
    val input = "let a_b : string = 'hello';"
    val version = "1.0"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)
    val rules = LinterRules(version, "snake-case", true)

    var errorList: List<LinterResult> = emptyList()

    var currentEnv = EnvironmentCreator.create(System.getenv())

    while (asts.hasNext()) {
      currentEnv = asts.setEnv(currentEnv)
      val statement = asts.next()
      val lintResult = Linter.lint(statement, rules, version)

      if (!lintResult.isValid()) {
        errorList = errorList + lintResult
      }

      currentEnv = asts.getEnv()
    }

    assertEquals(0, errorList.size)
  }

  @Test
  fun testDeclarationCamelCaseValid() {
    val input = "let aA : string = 'hello';"
    val version = "1.0"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)
    val rules = LinterRules(version, "camel-case", true)

    var errorList: List<LinterResult> = emptyList()

    var currentEnv = EnvironmentCreator.create(System.getenv())

    while (asts.hasNext()) {
      currentEnv = asts.setEnv(currentEnv)
      val statement = asts.next()
      val lintResult = Linter.lint(statement, rules, version)

      if (!lintResult.isValid()) {
        errorList = errorList + lintResult
      }

      currentEnv = asts.getEnv()
    }

    assertEquals(0, errorList.size)
  }

  @Test
  fun testDeclarationCamelCaseInvalid() {
    val input = "let aA : string = 'hello'; let a_b : string = 'hello';"
    val version = "1.0"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)
    val rules = LinterRules(version, "camel-case", false)

    var errorList: List<LinterResult> = emptyList()

    var currentEnv = EnvironmentCreator.create(System.getenv())

    while (asts.hasNext()) {
      currentEnv = asts.setEnv(currentEnv)
      val statement = asts.next()
      val lintResult = Linter.lint(statement, rules, version)

      if (!lintResult.isValid()) {
        errorList = errorList + lintResult
      }

      currentEnv = asts.getEnv()
    }

    assertEquals(1, errorList.size)
    assertEquals(
      "Errors found:\n" +
        "Variable names must be in camelCase at Line 1, symbol 32, got a_b.",
      errorList[0].message
    )
  }

  @Test
  fun testBlocks() {
    val input = "let a : boolean = true; if (a) { let c : string = 'hello'; } else { let a_b : string = 'bye'; };"
    val version = "1.1"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)
    val rules = LinterRules(version, "camel-case", false)

    var errorList: List<LinterResult> = emptyList()

    var currentEnv = EnvironmentCreator.create(System.getenv())

    while (asts.hasNext()) {
      currentEnv = asts.setEnv(currentEnv)
      val statement = asts.next()
      val lintResult = Linter.lint(statement, rules, version)

      if (!lintResult.isValid()) {
        errorList = errorList + lintResult
      }

      currentEnv = asts.getEnv()
    }

    assertEquals(1, errorList.size)
  }

  @Test
  fun testReadInputCallInvalid() {
    val simulatedInput = "mocked input"

    System.setIn(ByteArrayInputStream(simulatedInput.toByteArray()))

    val input = "let input: string = readInput(\"Enter\" + \"something\");"
    val version = "1.1"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)
    val rules = LinterRules(version, "camel-case", false, false)

    var errorList: List<LinterResult> = emptyList()

    var currentEnv = EnvironmentCreator.create(System.getenv())

    while (asts.hasNext()) {
      currentEnv = asts.setEnv(currentEnv)
      val statement = asts.next()
      val lintResult = Linter.lint(statement, rules, version)

      if (!lintResult.isValid()) {
        errorList = errorList + lintResult
      }

      currentEnv = asts.getEnv()
    }

    assertEquals(1, errorList.size)
    assertEquals(
      "Errors found:\n" +
        "readInput() statements must receive a literal or identifier, not an expression. At Line 1, symbol 29, got BINARY_EXPRESSION.",
      errorList[0].message
    )
  }

  @Test
  fun testReadInputCallValid() {
    val input = "let salame: string = 'hola'; readEnv('salame');"
    val version = "1.1"
    val tokens = Lexer.fromString(input, version)
    val asts = Parser(tokens, version)
    val rules = LinterRules(version, "camel-case", false, true)

    var errorList: List<LinterResult> = emptyList()

    var currentEnv = EnvironmentCreator.create(System.getenv())

    while (asts.hasNext()) {
      currentEnv = asts.setEnv(currentEnv)
      val statement = asts.next()
      val lintResult = Linter.lint(statement, rules, version)

      if (!lintResult.isValid()) {
        errorList = errorList + lintResult
      }

      currentEnv = asts.getEnv()
    }

    assertEquals(0, errorList.size)
  }
}
