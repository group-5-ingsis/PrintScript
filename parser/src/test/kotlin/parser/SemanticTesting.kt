package parser

import exception.SemanticErrorException
import lexer.Lexer
import org.junit.Test
import kotlin.test.assertFailsWith

class SemanticTesting {

    @Test
    fun testConstDeclarationShouldFail() {
        /* Declaration of const. */
        val lexer = Lexer("const a: string;")
        val parser = Parser(lexer)
        assertFailsWith(SemanticErrorException::class) {
            parser.next()
        }
    }

    @Test
    fun testAssignationShouldPass() {
        val lexer = Lexer("let a: string; a = \"testing\";")
        val parser = Parser(lexer)
        println(parser.next())
    }

    @Test
    fun testAssignationToDifferentTypeShouldFail() {
        val lexer = Lexer("let a: string; a = 22;")
        val parser = Parser(lexer)
        assertFailsWith(SemanticErrorException::class) {
            parser.next()
            parser.next()
        }
    }

    @Test
    fun testAssignationOfConstShouldFail() {
        val lexer = Lexer("const a: string = \"test\"; a = \"testing\";")
        val parser = Parser(lexer)
        assertFailsWith(SemanticErrorException::class) {
            parser.next()
        }
    }

    @Test
    fun testAssignationToInexistentVariableShouldFail() {
        val lexer = Lexer("let a: string; a = b;")
        val parser = Parser(lexer)
        assertFailsWith(SemanticErrorException::class) {
            parser.next()
        }
    }

    @Test
    fun testAssignationOfInexistentVariableShouldFail() {
        val lexer = Lexer("b = 23;")
        val parser = Parser(lexer)
        assertFailsWith(Error::class) {
            parser.next()
        }
    }

    @Test
    fun testAssignDeclareShouldPass() {
        val lexer = Lexer("let b: number = 23; let b: string;")
        val parser = Parser(lexer)
        println(parser.next())
    }

    @Test
    fun testAssignDeclareShouldFail() {
        val lexer = Lexer("const b: string = 23;")
        val parser = Parser(lexer)
        assertFailsWith(SemanticErrorException::class) {
            parser.next()
        }
    }
}
