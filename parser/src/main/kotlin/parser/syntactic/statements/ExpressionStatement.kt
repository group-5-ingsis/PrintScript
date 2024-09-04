package parser.syntactic.statements

import nodes.StatementType
import parser.syntactic.TokenManager
import parser.syntactic.expressions.ExpressionType
import token.Token

class ExpressionStatement() : StatementParser {
    override fun parse(tokens: List<Token>): ParseStatementResult {
        val (remainingTokens, exp) = ExpressionType.makeExpressionEvaluator().parse(tokens)
        val manager = TokenManager(remainingTokens)
        val position = manager.getPosition()
        manager.consumeTokenValue(";")

        return Pair(manager.getTokens(), StatementType.StatementExpression(exp, position))
    }
}
