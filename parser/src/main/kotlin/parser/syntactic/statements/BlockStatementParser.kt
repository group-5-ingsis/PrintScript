package parser.syntactic.statements

import nodes.Statement
import parser.syntactic.TokenManager
import token.Token

object BlockStatementParser : StatementParser {
    override fun parse(tokens: List<Token>): Statement {
        val statements = mutableListOf<Statement>()
        val position = TokenManager(tokens).getPosition()
        var manager = TokenManager(tokens)

        while (!manager.matches("RIGHT_BRACE") && manager.hasNext()) {
            val (remainingTokens, statement) = statementParser.parse(manager.getTokens())
            manager = TokenManager(remainingTokens)
            statements.add(statement)
        }
        manager.consumeTokenType("RIGHT_BRACE")
        return Pair(manager.getTokens(), Statement.BlockStatement(position, statements))
    }
}
