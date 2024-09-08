package parser

import exception.SemanticErrorException
import lexer.Lexer
import org.junit.Test
import kotlin.test.assertFailsWith

class SemanticTesting {

    @Test
    fun testConstDeclarationShouldFail() {
        /* Declaration of const. */
        val lexer = Lexer("const a: String;")
        val parser = Parser(lexer)
        assertFailsWith(SemanticErrorException::class) {
            parser.next()
        }
    }

    @Test
    fun testAssignationShouldPass() {
        val lexer = Lexer("let a: String; a = \"testing\";")
        val parser = Parser(lexer)
        println(parser.next())
    }

    @Test
    fun testAssignationToDifferentTypeShouldFail() {
        val lexer = Lexer("let a: String; a = 22;")
        val parser = Parser(lexer)
        assertFailsWith(SemanticErrorException::class) {
            parser.next()
        }
    }

    @Test
    fun testAssignationOfConstShouldFail() {
        val lexer = Lexer("const a: String = \"test\"; a = \"testing\";")
        val parser = Parser(lexer)
        assertFailsWith(SemanticErrorException::class) {
            parser.next()
        }
    }

    @Test
    fun testAssignationToInexistentVariableShouldFail() {
        val lexer = Lexer("let a: String; a = b;")
        val parser = Parser(lexer)
        assertFailsWith(SemanticErrorException::class) {
            parser.next()
        }
    }

    @Test
    fun testAssignationOfInexistentVariableShouldFail() {
        val lexer = Lexer("b = 23;")
        val parser = Parser(lexer)
        assertFailsWith(SemanticErrorException::class) {
            parser.next()
        }
    }

    @Test
    fun testAssignDeclareShouldPass() {
        val lexer = Lexer("let b: Number = 23;")
        val parser = Parser(lexer)
        println(parser.next())
    }

    @Test
    fun testAssignDeclareShouldFail() {
        val lexer = Lexer("const b: String = 23;")
        val parser = Parser(lexer)
        assertFailsWith(SemanticErrorException::class) {
            parser.next()
        }
    }
}
