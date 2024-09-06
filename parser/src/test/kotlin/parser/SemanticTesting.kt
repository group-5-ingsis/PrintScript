package parser

import lexer.TokenIterator
import org.junit.Test

class SemanticTesting {

    @Test
    fun testDeclarationShouldPass() {
        val tokenIterator = TokenIterator("let a: String;")
        val parser = Parser(tokenIterator)
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
