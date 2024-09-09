package token

import kotlin.test.Test
import kotlin.test.assertEquals

class SymbolProviderTest {
    @Test
    fun testGetKeywordsWithSimpleString() {
        val input = "word1 word2; word3"
        val expected =
            listOf(
                listOf("word1", "word2", ";", "word3")
            )
        val result = SymbolProvider.getKeywordMatrix(input)
        assertEquals(expected, result)
    }

    @Test
    fun testGetKeywordsWithQuotedText() {
        val input = "\"quoted text\" word1; word2"
        val expected =
            listOf(
                listOf("\"quoted text\"", "word1", ";", "word2")
            )
        val result = SymbolProvider.getKeywordMatrix(input)
        assertEquals(expected, result)
    }

    @Test
    fun testGetKeywordsWithEmptyString() {
        val input = ""
        val expected = listOf<List<String>>()
        val result = SymbolProvider.getKeywordMatrix(input)
        assertEquals(expected, result)
    }

    @Test
    fun testGetKeywordsWithOnlySymbols() {
        val input = ";-+*/="
        val expected =
            listOf(
                listOf(";", "-", "+", "*", "/", "=")
            )
        val result = SymbolProvider.getKeywordMatrix(input)
        assertEquals(expected, result)
    }

    @Test
    fun testGetKeywordsWithMultiLineString() {
        val input = "word1 word2;\nword3 \"quoted text\"\nword4"
        val expected =
            listOf(
                listOf("word1", "word2", ";"),
                listOf("word3", "\"quoted text\""),
                listOf("word4")
            )
        val result = SymbolProvider.getKeywordMatrix(input)
        assertEquals(expected, result)
    }
}
