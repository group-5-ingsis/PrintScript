package parser

import lexer.Lexer
import org.junit.Test

class SemanticTesting {

    @Test
    fun testDeclarationShouldPass() {
        val lexer = Lexer("let a: String;")
        val parser = Parser(lexer)
        println(parser.next())
    }

    @Test
    fun testDeclarationShouldFail() {
        /* Declaration of const. */
    }

    @Test
    fun testAssignationShouldPass() {
    }

    @Test
    fun testAssignationToDifferentTypeShouldFail() {
    }

    @Test
    fun testAssignationToConstShouldFail() {
    }

    @Test
    fun testAssignationToInexistentVariableShouldFail() {
    }

    @Test
    fun testAssignDeclareShouldPass() {
    }
}
