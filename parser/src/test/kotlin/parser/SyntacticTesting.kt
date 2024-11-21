package parser

import exception.InvalidSyntaxException
import lexer.Lexer
import org.junit.Test
import kotlin.test.assertFailsWith

class SyntacticTesting {

  @Test
  fun testDeclarationWithoutSemicolonShouldFail() {
    val lexer = Lexer.fromString("let a: string")
    val parser = Parser(lexer)
    assertFailsWith(InvalidSyntaxException::class) {
      println(parser.next())
    }
  }

  @Test
  fun testDeclarationWithParenthesisShouldFail() {
    val lexer = Lexer.fromString("let a: string(")
    val parser = Parser(lexer)
    assertFailsWith(InvalidSyntaxException::class) {
      println(parser.next())
    }
  }

  @Test
  fun testDeclarationShouldPass() {
    val lexer = Lexer.fromString("let a: string; const b: string = \"hello world\";")
    val parser = Parser(lexer)
    println(parser.next())
    println(parser.next())
  }
}
