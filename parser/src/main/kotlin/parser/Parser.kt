package parser

import Environment
import exception.SemanticErrorException
import nodes.StatementType
import parser.semantic.SemanticParser
import parser.syntactic.SyntacticParser
import token.Token

class Parser(private val lexer: Iterator<Token>, private val version: String = "1.1") : Iterator<StatementType> {

    var env = Environment()

    override fun hasNext(): Boolean {
        return lexer.hasNext()
    }

    override fun next(): StatementType {
        val mutableListTokensForParse: MutableList<Token> = mutableListOf()
        var lastException: Exception? = null

        while (lexer.hasNext()) {
            val token = lexer.next()
            mutableListTokensForParse.add(token)

            try {
                val (stm, tokens) = SyntacticParser.parse(mutableListTokensForParse, version)
                if (tokens.isEmpty()) {
                    env = SemanticParser.validate(stm, env)
                    return stm
                }
            } catch (e: SemanticErrorException) {
                throw e
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
