package parser

import environment.Environment
import nodes.StatementType
import parser.semantic.SemanticParser
import parser.syntactic.SyntacticParser
import position.visitor.InputProvider
import position.visitor.PrintScriptInputProvider
import token.Token

class Parser(
    private val lexer: Iterator<Token>,
    private val version: String = "1.1",
    private var readInput: InputProvider = PrintScriptInputProvider()
) : Iterator<StatementType> {
    private var env = Environment()
    private val momentList: ArrayDeque<Token> = ArrayDeque()

    override fun hasNext(): Boolean {
        return lexer.hasNext()
    }

    fun setEnv(env: Environment): Environment {
        this.env = env
        return env
    }

    fun getEnv(): Environment {
        return env
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
                val (stm, _) = SyntacticParser.parse(mutableListTokensForParse, version)
                ifChecker()

                env = SemanticParser.validate(stm, env, readInput)

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
            "No tokens to parse",
            "Expected ')' after expression in Line 0, symbol 0",
            "Expect this type: RIGHT_BRACE in Line 0, symbol 0"
        )

        return e.message in listMng
    }
}
