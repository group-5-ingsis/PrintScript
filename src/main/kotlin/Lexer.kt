import SymbolProvider.getKeywordMatrix
import token.TokenGenerator.generateToken
import token.Token

object Lexer {

    fun lex(input: String): List<Token> {
        // Calls a new method to hide implementation details
        return getTokenList(input)
    }

    private fun getTokenList(keywords: String): List<Token> {
        // Get the keyword matrix (keywords and positions represented by their index in the list of lists)
        val keywordMatrix : List<List<String>> = getKeywordMatrix(keywords)

        // Use keyword matrix to create a list of tokens
        val tokens: MutableList<Token> = mutableListOf()
        for (i in keywordMatrix.indices) {
            for (j in keywordMatrix[i].indices) {
                val keyword = keywordMatrix[i][j]
                tokens.add(generateToken(keyword, i, j))
            }
        }

        // Return the list of tokens
        return tokens
    }
}