package linter

import lexer.Lexer
import parser.Parser
import rules.LinterRulesV1
import rules.LinterRulesV2
import kotlin.test.Test
import kotlin.test.assertEquals

class LinterTests {

    @Test
    fun testPrintlnCallInvalid() {
        val input = "println(6 + 6);"
        val tokens = Lexer(input, "1.0")
        val asts = Parser(tokens, "1.0")
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
        val tokens = Lexer(input, "1.0")
        val asts = Parser(tokens, "1.0")
        val rules = LinterRulesV1("camel-case", true)
        val linter = Linter(rules)
        linter.lint(asts)
        assertEquals(0, linter.getErrors().size)
    }

    @Test
    fun testPrintlnCallValidWithIdentifier() {
        val input = "let a : string = 'hello' ;println(a);"
        val tokens = Lexer(input, "1.0")
        val asts = Parser(tokens, "1.0")
        val rules = LinterRulesV1("camel-case", false)
        val linter = Linter(rules)
        linter.lint(asts)
        println(linter.getErrors())
        assertEquals(0, linter.getErrors().size)
    }

    @Test
    fun testDeclarationOffValid() {
        val input = "let aA : string = 'hello'; let a_b : string = 'hello';"
        val tokens = Lexer(input, "1.0")
        val asts = Parser(tokens, "1.0")
        val rules = LinterRulesV1("off", false)
        val linter = Linter(rules)
        linter.lint(asts)
        println(linter.getErrors())
        assertEquals(0, linter.getErrors().size)
    }

    @Test
    fun testDeclarationSnake_Case() {
        val input = "let aA : string = 'hello'; let a_b : string = 'hello';"
        val tokens = Lexer(input, "1.0")
        val asts = Parser(tokens, "1.0")
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
        val input = "let aA : string = 'hello'; let a_b : string = 'hello';"
        val tokens = Lexer(input, "1.0")
        val asts = Parser(tokens, "1.0")
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

    @Test
    fun testBlocks() {
        val input = "let a : boolean = true; if (a) { let c : string = 'hello'; } else { let a_b : string = 'bye'; };"
        val tokens = Lexer(input, "1.1")
        val asts = Parser(tokens, "1.1")
        val rules = LinterRulesV2("camel-case", false)
        val linter = Linter(rules)
        linter.lint(asts)
        println(linter.getErrors())
        assertEquals(1, linter.getErrors().size)
    }

    @Test
    fun testReadInputCallInvalid() {
        val input = "readInput(6 + 6);"
        val tokens = Lexer(input, "1.1")
        val asts = Parser(tokens, "1.1")
        val rules = LinterRulesV2("camel-case", false, false)
        val linter = Linter(rules)
        linter.lint(asts)
        assertEquals(1, linter.getErrors().size)
        assertEquals(
            "readInput() statements must receive a literal or identifier, not an expression. At Line 1, symbol 1, got BINARY_EXPRESSION.",
            linter.getErrors()[0].getMessage()
        )
        println(linter.getErrors())
    }

    @Test
    fun testReadInputCallValid() {
        val input = "readInput(6 + 6);"
        val tokens = Lexer(input, "1.1")
        val asts = Parser(tokens, "1.1")
        val rules = LinterRulesV2("camel-case", false, true)
        val linter = Linter(rules)
        linter.lint(asts)
        assertEquals(0, linter.getErrors().size)
        println(linter.getErrors())
    }
}
