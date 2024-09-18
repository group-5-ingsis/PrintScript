package lexer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import position.Position
import token.Token
import kotlin.test.Test

class LexerTest {

    private fun collectTokens(lexer: Lexer): List<Token> {
        val tokens = mutableListOf<Token>()
        while (lexer.hasNext()) {
            tokens.add(lexer.next())
        }
        return tokens
    }

    // -----------------
    // --- 1.0 Tests ---
    // -----------------

    @Test
    fun testLexSimpleString() {
        val input = "let x = 10"
        val version = "1.0"
        val expected = listOf(
            Token("let", "DECLARATION_KEYWORD", Position(1, 1)),
            Token("x", "IDENTIFIER", Position(1, 5)),
            Token("=", "ASSIGNMENT", Position(1, 7)),
            Token("10", "NUMBER", Position(1, 8))
        )

        val tokens = Lexer(input, version)
        assertEquals(expected, collectTokens(tokens))
    }

    @Test
    fun testLexStringWithQuotes() {
        val input = "let str = \"Hello\""
        val version = "1.0"
        val expected = listOf(
            Token("let", "DECLARATION_KEYWORD", Position(1, 1)),
            Token("str", "IDENTIFIER", Position(1, 5)),
            Token("=", "ASSIGNMENT", Position(1, 9)),
            Token("Hello", "STRING", Position(1, 10))
        )

        val tokens = Lexer(input, version)
        assertEquals(expected, collectTokens(tokens))
    }

    @Test
    fun testLexStringWithSymbols() {
        val input = "x + y - z"
        val version = "1.0"
        val expected = listOf(
            Token("x", "IDENTIFIER", Position(1, 1)),
            Token("+", "OPERATOR", Position(1, 3)),
            Token("y", "IDENTIFIER", Position(1, 5)),
            Token("-", "OPERATOR", Position(1, 7)),
            Token("z", "IDENTIFIER", Position(1, 8))
        )

        val tokens = Lexer(input, version)
        assertEquals(expected, collectTokens(tokens))
    }

    @Test
    fun testLexMultiLineString() {
        val input = "let x = 10;\nlet y = 20;"
        val version = "1.0"
        val expected = listOf(
            Token("let", "DECLARATION_KEYWORD", Position(1, 1)),
            Token("x", "IDENTIFIER", Position(1, 5)),
            Token("=", "ASSIGNMENT", Position(1, 7)),
            Token("10", "NUMBER", Position(1, 9)),
            Token(";", "PUNCTUATION", Position(1, 10)),
            Token("let", "DECLARATION_KEYWORD", Position(2, 1)),
            Token("y", "IDENTIFIER", Position(2, 5)),
            Token("=", "ASSIGNMENT", Position(2, 7)),
            Token("20", "NUMBER", Position(2, 9)),
            Token(";", "PUNCTUATION", Position(2, 10))
        )

        val tokens = Lexer(input, version)
        assertEquals(expected, collectTokens(tokens))
    }

    @Test
    fun testLexerUnknownCharacters() {
        val input = "let a @ ball"
        val version = "1.0"
        val tokens = Lexer(input, version)

        assertThrows(IllegalArgumentException::class.java) {
            collectTokens(tokens)
        }
    }

    @Test
    fun testSimpleQuotes() {
        val input = "let x = 'hello'"
        val version = "1.0"
        val expected = listOf(
            Token("let", "DECLARATION_KEYWORD", Position(1, 1)),
            Token("x", "IDENTIFIER", Position(1, 5)),
            Token("=", "ASSIGNMENT", Position(1, 7)),
            Token("hello", "STRING", Position(1, 8))
        )

        val tokens = Lexer(input, version)
        assertEquals(expected, collectTokens(tokens))
    }

    // -----------------
    // --- 1.1 Tests ---
    // -----------------

    @Test
    fun testLexConstVariableDeclaration() {
        val input = "const pi: number = 3.14;"
        val version = "1.1"
        val tokens = Lexer(input, version)

        val expected = listOf(
            Token("const", "CONST", Position(1, 1)),
            Token("pi", "IDENTIFIER", Position(1, 7)),
            Token(":", "PUNCTUATION", Position(1, 8)),
            Token("number", "VARIABLE_TYPE", Position(1, 11)),
            Token("=", "ASSIGNMENT", Position(1, 18)),
            Token("3.14", "NUMBER", Position(1, 20)),
            Token(";", "PUNCTUATION", Position(1, 23))
        )

        assertEquals(expected, collectTokens(tokens))
    }

    @Test
    fun testLexBooleanTypes() {
        val input = "let isTrue: boolean = true; isFalse = false;"
        val version = "1.1"
        val tokens = Lexer(input, version)
        val expected = listOf(
            Token("let", "LET", Position(1, 1)),
            Token("isTrue", "IDENTIFIER", Position(1, 5)),
            Token(":", "PUNCTUATION", Position(1, 10)),
            Token("boolean", "VARIABLE_TYPE", Position(1, 13)),
            Token("=", "ASSIGNMENT", Position(1, 21)),
            Token("true", "BOOLEAN", Position(1, 23)),
            Token(";", "PUNCTUATION", Position(1, 26)),
            Token("isFalse", "IDENTIFIER", Position(1, 29)),
            Token("=", "ASSIGNMENT", Position(1, 37)),
            Token("false", "BOOLEAN", Position(1, 39)),
            Token(";", "PUNCTUATION", Position(1, 43))
        )

        assertEquals(expected, collectTokens(tokens))
    }

    @Test
    fun testLexIfElseStatement() {
        val input = "if (true) { y = 5; } else { y = 0; }"
        val version = "1.1"
        val tokens = Lexer(input, version)

        val expected = listOf(
            Token("if", "IF", Position(1, 1)),
            Token("(", "PUNCTUATION", Position(1, 4)),
            Token("true", "BOOLEAN", Position(1, 5)),
            Token(")", "PUNCTUATION", Position(1, 8)),
            Token("{", "LEFT_BRACE", Position(1, 11)),
            Token("y", "IDENTIFIER", Position(1, 13)),
            Token("=", "ASSIGNMENT", Position(1, 15)),
            Token("5", "NUMBER", Position(1, 17)),
            Token(";", "PUNCTUATION", Position(1, 17)),
            Token("}", "RIGHT_BRACE", Position(1, 20)),
            Token("else", "ELSE", Position(1, 22)),
            Token("{", "LEFT_BRACE", Position(1, 27)),
            Token("y", "IDENTIFIER", Position(1, 29)),
            Token("=", "ASSIGNMENT", Position(1, 31)),
            Token("0", "NUMBER", Position(1, 33)),
            Token(";", "PUNCTUATION", Position(1, 33)),
            Token("}", "RIGHT_BRACE", Position(1, 36))
        )

        assertEquals(expected, collectTokens(tokens))
    }

    @Test
    fun testLexReadInputAndReadEnvMethods() {
        val input = "readInput(); readEnv();"
        val version = "1.1"
        val tokens = Lexer(input, version)

        val expected = listOf(
            Token("readInput", "READ_INPUT", Position(1, 1)),
            Token("(", "PUNCTUATION", Position(1, 9)),
            Token(")", "PUNCTUATION", Position(1, 11)),
            Token(";", "PUNCTUATION", Position(1, 12)),
            Token("readEnv", "READ_ENV", Position(1, 14)),
            Token("(", "PUNCTUATION", Position(1, 20)),
            Token(")", "PUNCTUATION", Position(1, 22)),
            Token(";", "PUNCTUATION", Position(1, 23))
        )

        assertEquals(expected, collectTokens(tokens))
    }
}
