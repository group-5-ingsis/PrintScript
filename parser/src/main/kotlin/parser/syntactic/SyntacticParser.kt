package parser.syntactic

import nodes.Statement
import token.Token

object SyntacticParser {

    fun parse(tokens: List<Token>, version: String): Statement {
        val tokenManager = TokenManager(tokens)
        val statementParser = ParserFactory.createStatementParser(tokenManager, version)
        val statement = statementParser.parse(tokenManager)
        return statement
    }
}
