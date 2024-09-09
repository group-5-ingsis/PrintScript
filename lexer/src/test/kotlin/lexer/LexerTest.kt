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

    @Test
    fun testLexSimpleString() {
        val input = "let x = 10"
        val expected = listOf(
            Token("let", "DECLARATION_KEYWORD", Position(1, 1)),
            Token("x", "IDENTIFIER", Position(1, 5)),
            Token("=", "ASSIGNMENT", Position(1, 7)),
            Token("10", "NUMBER", Position(1, 8))
        )

        val lexer = LexerBuilder().withInput(input).withVersion("1.0").build()
        val actual = collectTokens(lexer)
        assertEquals(expected, actual)
    }

    @Test
    fun testLexStringWithQuotes() {
        val input = "let str = \"Hello\""
        val expected = listOf(
            Token("let", "DECLARATION_KEYWORD", Position(1, 1)),
            Token("str", "IDENTIFIER", Position(1, 5)),
            Token("=", "ASSIGNMENT", Position(1, 8)),
            Token("\"Hello\"", "STRING", Position(1, 10))
        )

        val lexer = LexerBuilder().withInput(input).withVersion("1.0").build()
        val actual = collectTokens(lexer)
        assertEquals(expected, actual)
    }

    @Test
    fun testLexStringWithSymbols() {
        val input = "x + y - z"
        val expected = listOf(
            Token("x", "IDENTIFIER", Position(1, 1)),
            Token("+", "OPERATOR", Position(1, 3)),
            Token("y", "IDENTIFIER", Position(1, 5)),
            Token("-", "OPERATOR", Position(1, 7)),
            Token("z", "IDENTIFIER", Position(1, 9))
        )

        val lexer = LexerBuilder().withInput(input).withVersion("1.0").build()
        val actual = collectTokens(lexer)
        assertEquals(expected, actual)
    }

    @Test
    fun testLexMultiLineString() {
        val input = "let x = 10;\nlet y = 20;"
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

        val lexer = LexerBuilder().withInput(input).withVersion("1.0").build()
        val actual = collectTokens(lexer)
        assertEquals(expected, actual)
    }

    @Test
    fun testLexerUnknownCharacters() {
        val input = "let a @ ball"
        val lexer = LexerBuilder().withInput(input).build()

        assertThrows(IllegalArgumentException::class.java) {
            collectTokens(lexer)
        }
    }

    @Test
    fun testSimpleQuotes() {
        val input = "let x = 'hello'"
        val expected = listOf(
            Token("let", "DECLARATION_KEYWORD", Position(1, 1)),
            Token("x", "IDENTIFIER", Position(1, 5)),
            Token("=", "ASSIGNMENT", Position(1, 7)),
            Token("'hello'", "STRING", Position(1, 9))
        )

        val lexer = LexerBuilder().withInput(input).withVersion("1.0").build()
        val actual = collectTokens(lexer)
        assertEquals(expected, actual)
    }
}
