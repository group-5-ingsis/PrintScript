package lexer

import SymbolProvider.getKeywordMatrix
import token.TokenGenerator.generateToken
import token.Token

object Lexer {

    fun lex(input: String, paramsList: List<String>): List<Token> {
        return getTokenList(input)
    }

    private fun getTokenList(keywords: String): List<Token> {

        val keywordMatrix : List<List<String>> = getKeywordMatrix(keywords)

        val tokens: MutableList<Token> = mutableListOf()

        val lines = keywordMatrix.indices

        for (line in lines) {

            val words = keywordMatrix[line].indices

            for (wordPosition in words) {

                val keyword = keywordMatrix[line][wordPosition]

                val token = generateToken(keyword, line, wordPosition)

                tokens.add(token)
            }
        }

        return tokens
    }
}