package parser

import exceptions.BadSyntacticException
import lexer.Lexer
import org.junit.Test
import kotlin.test.assertFailsWith

class SyntacticTesting {

    @Test
    fun testDeclarationWithoutSemicolonShouldFail() {
        val lexer = Lexer("let a: string")
        val parser = Parser(lexer)
        assertFailsWith(BadSyntacticException::class) {
            println(parser.next())
        }
    }

    @Test
    fun testDeclarationWithParenthesisShouldFail() {
        val lexer = Lexer("let a: string(")
        val parser = Parser(lexer)
        assertFailsWith(BadSyntacticException::class) {
            println(parser.next())
        }
    }

    @Test
    fun testDeclarationShouldPass() {
        val lexer = Lexer("let a: string;")
        val parser = Parser(lexer)
        println(parser.next())
    }
}
