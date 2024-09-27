package parser.syntactic.statements

import nodes.Statement
import parser.syntactic.TokenManager
import token.Token

object ExpressionStatementParser : StatementParser {
    override fun parse(tokens: List<Token>): ParseStatementResult {
        val (remainingTokens, exp) = expressionEvaluator.parse(tokens)
        val manager = TokenManager(remainingTokens)
        val position = manager.getPosition()
        manager.consumeTokenValue(";")

        return Pair(manager.getTokens(), Statement.StatementExpression(exp, position))
    }
}
