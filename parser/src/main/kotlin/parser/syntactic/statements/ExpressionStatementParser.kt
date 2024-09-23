package parser.syntactic.statements

import ExpressionType
import nodes.StatementType
import parser.syntactic.TokenManager

import token.Token

class ExpressionStatementParser(private val expressionEvaluator: ExpressionType) : StatementParser {
    override fun parse(tokens: List<Token>): ParseStatementResult {
        val (remainingTokens, exp) = expressionEvaluator.parse(tokens)
        val manager = TokenManager(remainingTokens)
        val position = manager.getPosition()
        manager.consumeTokenValue(";")

        return Pair(manager.getTokens(), StatementType.StatementExpression(exp, position))
    }
}
