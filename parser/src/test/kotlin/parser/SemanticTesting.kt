package parser

import lexer.Lexer
import org.junit.Test

class SemanticTesting {

    @Test
    fun testDeclarationShouldPass() {
        val lexer = Lexer("let a: String")
        val parser = Parser(lexer)
        println(parser.next())
    }

    @Test
    fun testDeclarationShouldFail() {
        /* Declaration of const. */
        val lexer = Lexer("const a: String;")
        val parser = Parser(lexer)
        println(parser.next())
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
    }

    @Test
    fun testAssignationToConstShouldFail() {
        val lexer = Lexer("const a: String = \"test\"; a = \"testing\";")
        val parser = Parser(lexer)
    }

    @Test
    fun testAssignationToInexistentVariableShouldFail() {
        val lexer = Lexer("let a: String; a = b;")
        val parser = Parser(lexer)
    }

    @Test
    fun testAssignationOfInexistentVariableShouldFail() {
        val lexer = Lexer("b = 23;")
        val parser = Parser(lexer)
    }

    @Test
    fun testAssignDeclareShouldPass() {
        val lexer = Lexer("let b: Number = 23;")
        val parser = Parser(lexer)
        println(parser.next())
    }

    @Test
    fun testAssignDeclareShouldFail() {
        val lexer = Lexer("let b: String = 23;")
        val parser = Parser(lexer)
    }
}
