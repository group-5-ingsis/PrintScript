package linter

import lexer.Lexer
import parser.Parser
import rules.LinterRulesV1
import kotlin.test.Test
import kotlin.test.assertEquals

class LinterTests {

    private val version = "1.0"

    @Test
    fun testPrintlnCallInvalid() {
        val input = "println(6 + 6);"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val rules = LinterRulesV1("camel-case", false)
        val linter = Linter(rules)
        linter.lint(asts)
        assertEquals(1, linter.getErrors().size)
        assertEquals(
            "println() statements must receive a literal or identifier expression at Line 1, symbol 7, got BINARY_EXPRESSION.",
            linter.getErrors()[0].getMessage()
        )
        println(linter.getErrors())
    }

    @Test
    fun testPrintlnCallValid() {
        val input = "println(6 + 6);"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val rules = LinterRulesV1("camel-case", true)
        val linter = Linter(rules)
        linter.lint(asts)
        assertEquals(0, linter.getErrors().size)
    }

    @Test
    fun testPrintlnCallValidWithIdentifier() {
        val input = "let a : String = 'hello' ;println(a);"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val rules = LinterRulesV1("camel-case", false)
        val linter = Linter(rules)
        linter.lint(asts)
        println(linter.getErrors())
        assertEquals(0, linter.getErrors().size)
    }

    @Test
    fun testDeclarationOffValid() {
        val input = "let aA : String = 'hello'; let a_b : String = 'hello';"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val rules = LinterRulesV1("off", false)
        val linter = Linter(rules)
        linter.lint(asts)
        println(linter.getErrors())
        assertEquals(0, linter.getErrors().size)
    }

    @Test
    fun testDeclarationSnake_Case() {
        val input = "let aA : String = 'hello'; let a_b : String = 'hello';"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val rules = LinterRulesV1("snake-case", false)
        val linter = Linter(rules)
        linter.lint(asts)
        println(linter.getErrors())
        assertEquals(1, linter.getErrors().size)
        assertEquals(
            "Variable names must be in snake_case at Line 1, symbol 5, got aA.",
            linter.getErrors()[0].getMessage()
        )
    }

    @Test
    fun testDeclarationCamelCase() {
        val input = "let aA : String = 'hello'; let a_b : String = 'hello';"
        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)
        val rules = LinterRulesV1("camel-case", false)
        val linter = Linter(rules)
        linter.lint(asts)
        println(linter.getErrors())
        assertEquals(1, linter.getErrors().size)
        assertEquals(
            "Variable names must be in camelCase at Line 1, symbol 32, got a_b.",
            linter.getErrors()[0].getMessage()
        )
    }
}
