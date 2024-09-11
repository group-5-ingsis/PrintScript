package parser

import Environment
import nodes.StatementType
import parser.semantic.SemanticParser
import parser.syntactic.SyntacticParser
import token.Token

class Parser(private val lexer: Iterator<Token>, private val version: String = "1.1") : Iterator<StatementType> {
    private var env = Environment()
    private val momentList: ArrayDeque<Token> = ArrayDeque()

    override fun hasNext(): Boolean {
        return lexer.hasNext()
    }

    override fun next(): StatementType {
        val mutableListTokensForParse: MutableList<Token> = mutableListOf()
        var lastException: Exception? = null

        while (lexer.hasNext() || momentList.isNotEmpty()) {
            if (momentList.isNotEmpty()) {
                mutableListTokensForParse.add(momentList.removeFirst())
            } else {
                mutableListTokensForParse.add(lexer.next())
            }

            try {
                val (stm, tokens) = SyntacticParser.parse(mutableListTokensForParse, version)
                ifChecker()

                if (tokens.isEmpty()) {
                    env = SemanticParser.validate(stm, env)
                    return stm
                }

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

    private fun ifChecker() {
        if (lexer.hasNext()) {
            val next = lexer.next()
            momentList.add(next)
            if (next.type == "ELSE") {
                throw Exception("Exeption for continue the loop")
            }
        }
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
