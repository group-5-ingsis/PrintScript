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
    private var readInput: InputProvider = PrintScriptInputProvider(),
    private var env: Environment = Environment()
) : Iterator<Statement> {

    var tokens: List<Token> = listOf()

    override fun hasNext(): Boolean {
        return lexer.hasNext()
    }

    override fun next(): Statement {
        var lastException: Exception? = null

        while (lexer.hasNext()) {
            val nextToken = lexer.next()
            tokens = tokens + nextToken

            try {
                val statement = SyntacticParser.parse(tokens, version)

                statement = SemanticParser.validate(statement, env, readInput)

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

    private fun allowedException(e: Exception): Boolean {
        val exception = e.message

        val allowedExceptions = listOf(
            "No tokens to get position from.",
            "Find unknown expression at line: 0 and at index: 0",
            "No tokens to parse",
            "Expected ')' after expression in Line 0, symbol 0",
            "Expect this type: RIGHT_BRACE in Line 0, symbol 0"
        )

        return exception in allowedExceptions
    }
}
