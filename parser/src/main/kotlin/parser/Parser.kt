
package parser

import nodes.StatementType
import parser.syntactic.SyntacticParser
import token.Token

/**
 * A parser that takes an iterator of tokens and produces an iterator of statement types.
 *
 * @property lexer An iterator of tokens to be parsed.
 */
class Parser(private val lexer: Iterator<Token>) : Iterator<StatementType> {

    /**
     * Checks if there are more tokens to be parsed.
     *
     * @return `true` if there are more tokens, `false` otherwise.
     */
    override fun hasNext(): Boolean {
        return lexer.hasNext()
    }

    /**
     * Parses the next statement from the tokens.
     *
     * @return The next parsed statement.
     * @throws NoSuchElementException if there are no more tokens available to parse.
     */
    override fun next(): StatementType {
        val mutableListTokensForParse: MutableList<Token> = mutableListOf()
        var lastException: Exception? = null

        while (lexer.hasNext()) {
            mutableListTokensForParse.add(lexer.next())

            try {
                val (stm, tokens) = SyntacticParser.parse(mutableListTokensForParse)
                if (tokens.isEmpty()) {
                    return stm
                }
            } catch (e: Exception) {
                if (!isAlowedExeption(e)) {
                    if (lastException != null && lastException::class == e::class && lastException.message == e.message) {
                        throw e
                    }
                }

                lastException = e
            }
        }

        throw NoSuchElementException("No more tokens available to parse")
    }

    private fun isAlowedExeption(e: Exception): Boolean {
        val listMng = listOf(
            "No tokens to get position from.",
            "Find unknown expression at line: 0 and at index: 0",
            "No tokens to parse"
        )

        return e.message in listMng
    }
}
