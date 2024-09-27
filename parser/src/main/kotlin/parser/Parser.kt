package parser

import environment.Environment
import exception.AllowedException
import nodes.Statement
import parser.syntactic.SyntacticParser
import token.Token
import visitor.InputProvider
import visitor.PrintScriptInputProvider

class Parser(
    private val lexer: Iterator<Token>,
    private val version: String = "1.1",
    private var readInput: InputProvider = PrintScriptInputProvider(),
    private var environment: Environment = Environment()
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

                // statement = SemanticParser.validate(statement, env, readInput)

                return statement
            } catch (e: Exception) {
                if (!lexer.hasNext()) {
                    throw e
                }
                if (e !is AllowedException) {
                    if (lastException != null && lastException::class == e::class && lastException.message == e.message) {
                        throw e
                    }
                }

                lastException = e
            }
        }

        throw NoSuchElementException("No more tokens available to parse")
    }
}
