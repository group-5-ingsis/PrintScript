package lexer

import SymbolProvider.getKeywordMatrix
import token.TokenGenerator.generateToken
import token.Token

object Lexer {

    fun lex(input: String, parameters: List<String>): List<Token> {
        return getTokenList(input)
    }

    private fun getTokenList(keywords: String): List<Token> {
        val keywordMatrix : List<List<String>> = getKeywordMatrix(keywords)

        val tokens: MutableList<Token> = mutableListOf()
        for (i in keywordMatrix.indices) {
            for (j in keywordMatrix[i].indices) {
                val keyword = keywordMatrix[i][j]
                tokens.add(generateToken(keyword, i, j))
            }
        }

        return tokens
    }
}