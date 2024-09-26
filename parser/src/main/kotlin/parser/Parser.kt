package parser

import environment.Environment
import nodes.Statement
import parser.semantic.SemanticParser
import parser.syntactic.SyntacticParser
import token.Token
import visitor.InputProvider
import visitor.PrintScriptInputProvider

class Parser(
    private val lexer: Iterator<Token>,
    private val version: String = "1.1",
    private var readInput: InputProvider = PrintScriptInputProvider()
) : Iterator<Statement> {
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

    override fun next(): Statement {
        var tokensForParsing: List<Token> = momentList.toList()
        var lastException: Exception? = null

        while (lexer.hasNext() || momentList.isNotEmpty()) {
            if (momentList.isNotEmpty()) {
                val firstToken = momentList.removeFirst()
                tokensForParsing = tokensForParsing + firstToken
            } else {
                val nextToken = lexer.next()
                tokensForParsing = tokensForParsing + nextToken
            }

            try {
                val statement = SyntacticParser.parse(tokensForParsing, version)
                ifChecker()

                env = SemanticParser.validate(statement, env, readInput)

                return statement
            } catch (e: Exception) {
                if (!lexer.hasNext()) {
                    throw e
                }
                if (!allowedException(e)) {
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
        val hasNext = lexer.hasNext()
        if (hasNext) {
            val next = lexer.next()
            momentList.add(next)
            if (next.type == "ELSE") {
                throw Exception("Exception for continue the loop")
            }
        }
    }

    private fun allowedException(e: Exception): Boolean {
        val allowedExceptions = listOf(
            "No tokens to get position from.",
            "Find unknown expression at line: 0 and at index: 0",
            "No tokens to parse",
            "Expected ')' after expression in Line 0, symbol 0",
            "Expect this type: RIGHT_BRACE in Line 0, symbol 0"
        )

        val exception = e.message
        return exception in allowedExceptions
    }
}
