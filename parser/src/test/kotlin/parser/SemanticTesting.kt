package parser

import exception.SemanticErrorException
import lexer.Lexer
import org.junit.Test
import kotlin.test.assertFailsWith

class SemanticTesting {

  @Test
  fun testConstDeclarationShouldFail() {
    val lexer = Lexer.fromString("const a: string;")
    val parser = Parser(lexer)
    assertFailsWith(SemanticErrorException::class) {
      parser.next()
    }
  }

  @Test
  fun testAssignationShouldPass() {
    val lexer = Lexer.fromString("let a: string; a = \"testing\";")
    val parser = Parser(lexer)
    val statementType = parser.next()
    println(statementType)
  }

  @Test
  fun testAssignationToDifferentTypeShouldFail() {
    val lexer = Lexer.fromString("let a: string; a = 22;")
    val parser = Parser(lexer)
    assertFailsWith(SemanticErrorException::class) {
      parser.next()
      parser.next()
    }
  }

  @Test
  fun testAssignationOfConstShouldFail() {
    val lexer = Lexer.fromString("const a: string = \"test\"; a = \"testing\";")
    val parser = Parser(lexer)
    assertFailsWith(Exception::class) {
      parser.next()
      parser.next()
    }
  }

  @Test
  fun testAssignationToInExistentVariableShouldFail() {
    val lexer = Lexer.fromString("let a: string; a = b;")
    val parser = Parser(lexer)
    assertFailsWith(Error::class) {
      parser.next()
      parser.next()
    }
  }

  @Test
  fun testAssignationOfInExistentVariableShouldFail() {
    val lexer = Lexer.fromString("b = 23;")
    val parser = Parser(lexer)
    assertFailsWith(Error::class) {
      parser.next()
    }
  }

  @Test
  fun testAssignDeclareShouldPass() {
    val lexer = Lexer.fromString("let b: number = 23; let b: string;")
    val parser = Parser(lexer)
    println(parser.next())
  }

  @Test
  fun testAssignDeclareShouldFail() {
    val lexer = Lexer.fromString("const b: string = 23;")
    val parser = Parser(lexer)
    assertFailsWith(SemanticErrorException::class) {
      parser.next()
    }
  }

  @Test
  fun testPrintlnCorrect() {
    val lexer = Lexer.fromString("let b: string = 23; println(b)")
    Parser(lexer)
  }

  @Test
  fun testPrintlnMultipleArguments() {
    val lexer = Lexer.fromString("let b: string = 23; println(b, 2)")
    Parser(lexer)
  }

  @Test
  fun testPrintlnUnknownVariable() {
    val tokens = Lexer.fromString("let something: string; println(something);")
    val parser = Parser(tokens)
    assertFailsWith(SemanticErrorException::class) {
      parser.next()
      parser.next()
    }
  }

  @Test
  fun testPrintVariable() {
    val tokens = Lexer.fromString("let something: number = 23; println(something);")
    val parser = Parser(tokens)
    parser.next()
    parser.next()
  }

  @Test
  fun testPrintNumber() {
    val tokens = Lexer.fromString("println(1);")
    val parser = Parser(tokens)
    parser.next()
  }

  @Test
  fun testPrintString() {
    val tokens = Lexer.fromString("println(\"Hello World!\");")
    val parser = Parser(tokens)
    parser.next()
  }

  @Test
  fun testPrintSum() {
    val tokens = Lexer.fromString("println(3 + 4);")
    val parser = Parser(tokens)
    parser.next()
  }
}
