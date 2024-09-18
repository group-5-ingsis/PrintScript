package lexer

import org.junit.Assert.assertEquals
import position.Position
import token.Token
import kotlin.test.Test

class TypeHandlingTests {

    private fun collectTokens(lexer: Lexer): List<Token> {
        val tokens = mutableListOf<Token>()
        while (lexer.hasNext()) {
            tokens.add(lexer.next())
        }
        return tokens
    }

    @Test
    fun testLexConstVariableDeclaration() {
        val input = "const pi: number = 3.14;"
        val expected = listOf(
            Token("const", "CONST", Position(1, 1)),
            Token("pi", "IDENTIFIER", Position(1, 7)),
            Token(":", "PUNCTUATION", Position(1, 8)),
            Token("number", "VARIABLE_TYPE", Position(1, 11)),
            Token("=", "ASSIGNMENT", Position(1, 18)),
            Token("3.14", "NUMBER", Position(1, 20)),
            Token(";", "PUNCTUATION", Position(1, 23))
        )

        val tokens = Lexer(input)
        assertEquals(expected, collectTokens(tokens))
    }

    @Test
    fun testLexBooleanTypes() {
        val input = "let isTrue: boolean = true; isFalse = false;"
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

        val tokens = Lexer(input)
        assertEquals(expected, collectTokens(tokens))
    }
}
