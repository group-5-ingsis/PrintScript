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

        Linter.clearResults()

        while (asts.hasNext()) {
            val statement = asts.next()
            Linter.lint(statement, rules)
        }

        val linterResults = Linter.getErrors()
        val numOfErrors = linterResults.size
        assertEquals(1, numOfErrors)
        assertEquals(
            "println() statements must receive a literal or identifier, not an expression. At Line 1, symbol 7, got BINARY_EXPRESSION.",
            linterResults[0].getMessage()
        )
    }

    @Test
    fun testPrintlnCallValid() {
        val input = "println(6 + 6);"
        val tokens = Lexer(input, "1.0")
        val asts = Parser(tokens, "1.0")
        val rules = LinterRulesV1("camel-case", true)

        Linter.clearResults()

        while (asts.hasNext()) {
            val statement = asts.next()
            Linter.lint(statement, rules)
        }

        assertEquals(0, Linter.getErrors().size)
    }

    @Test
    fun testPrintlnCallValidWithIdentifier() {
        val input = "let a : string = 'hello'; println(a);"
        val tokens = Lexer(input, "1.0")
        val asts = Parser(tokens, "1.0")
        val rules = LinterRulesV1("camel-case", false)

        Linter.clearResults()

        while (asts.hasNext()) {
            val statement = asts.next()
            Linter.lint(statement, rules)
        }

        assertEquals(0, Linter.getErrors().size)
    }

    @Test
    fun testDeclarationOffValid() {
        val input = "let aA : string = 'hello'; let a_b : string = 'hello';"
        val tokens = Lexer(input, "1.0")
        val asts = Parser(tokens, "1.0")
        val rules = LinterRulesV1("off", false)

        Linter.clearResults()

        while (asts.hasNext()) {
            val statement = asts.next()
            Linter.lint(statement, rules)
        }

        assertEquals(0, Linter.getErrors().size)
    }

    @Test
    fun testDeclarationSnake_Case() {
        val input = "let aA : string = 'hello'; let a_b : string = 'hello';"
        val tokens = Lexer(input, "1.0")
        val asts = Parser(tokens, "1.0")
        val rules = LinterRulesV1("snake-case", false)

        Linter.clearResults()

        while (asts.hasNext()) {
            val statement = asts.next()
            Linter.lint(statement, rules)
        }

        assertEquals(1, Linter.getErrors().size)
        assertEquals(
            "Variable names must be in snake_case at Line 1, symbol 5, got aA.",
            Linter.getErrors()[0].getMessage()
        )
    }

    @Test
    fun testDeclarationCamelCase() {
        val input = "let aA : string = 'hello'; let a_b : string = 'hello';"
        val tokens = Lexer(input, "1.0")
        val asts = Parser(tokens, "1.0")
        val rules = LinterRulesV1("camel-case", false)

        Linter.clearResults()

        while (asts.hasNext()) {
            val statement = asts.next()
            Linter.lint(statement, rules)
        }

        assertEquals(1, Linter.getErrors().size)
        assertEquals(
            "Variable names must be in camelCase at Line 1, symbol 32, got a_b.",
            Linter.getErrors()[0].getMessage()
        )
    }

    @Test
    fun testBlocks() {
        val input = "let a : boolean = true; if (a) { let c : string = 'hello'; } else { let a_b : string = 'bye'; };"
        val tokens = Lexer(input, "1.1")
        val asts = Parser(tokens, "1.1")
        val rules = LinterRulesV2("camel-case", false)

        Linter.clearResults()

        while (asts.hasNext()) {
            val statement = asts.next()
            Linter.lint(statement, rules)
        }

        assertEquals(1, Linter.getErrors().size)
    }

    @Test
    fun testReadInputCallInvalid() {
        val input = "let input: string = readInput(\"Enter\" + \"something\");"
        val tokens = Lexer(input, "1.1")
        val asts = Parser(tokens, "1.1")
        val rules = LinterRulesV2("camel-case", false, false)

        Linter.clearResults()

        while (asts.hasNext()) {
            val statement = asts.next()
            Linter.lint(statement, rules)
        }

        assertEquals(1, Linter.getErrors().size)
        assertEquals(
            "readInput() statements must receive a literal or identifier, not an expression. At Line 1, symbol 21, got BINARY_EXPRESSION.",
            Linter.getErrors()[0].getMessage()
        )
    }

    @Test
    fun testReadInputCallValid() {
        val input = "readInput(6 + 6);"
        val tokens = Lexer(input, "1.1")
        val asts = Parser(tokens, "1.1")
        val rules = LinterRulesV2("camel-case", false, true)

        Linter.clearResults()

        while (asts.hasNext()) {
            val statement = asts.next()
            Linter.lint(statement, rules)
        }

        assertEquals(0, Linter.getErrors().size)
    }
}
