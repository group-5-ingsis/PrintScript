package parser

import nodes.StatementType
import parser.syntactic.SyntacticParser
import token.Token

class Parser(private val lexer: Iterator<Token>, private val version: String = "1.1") : Iterator<StatementType> {

    override fun hasNext(): Boolean {
        return lexer.hasNext()
    }

    override fun next(): StatementType {
        val mutableListTokensForParse: MutableList<Token> = mutableListOf()
        var lastException: Exception? = null

        while (lexer.hasNext()) {
            mutableListTokensForParse.add(lexer.next())

            try {
                val (stm, tokens) = SyntacticParser.parse(mutableListTokensForParse, version)

                    return stm

            } catch (e: Exception) {
                if (!lexer.hasNext()) {
                    throw e
                }
                if (!isAllowedException(e)) {
                    if (lastException != null && lastException::class == e::class && lastException.message == e.message) {
                        throw e
                    }
                }

                lastException = e
            }
        }

        // TODO change. Add SemanticParser validation.
        throw NoSuchElementException("No more tokens available to parse")
    }

    private fun isAllowedException(e: Exception): Boolean {
        val listMng = listOf(
            "No tokens to get position from.",
            "Find unknown expression at line: 0 and at index: 0",
            "No tokens to parse"
        )

        return e.message in listMng
    }
}
