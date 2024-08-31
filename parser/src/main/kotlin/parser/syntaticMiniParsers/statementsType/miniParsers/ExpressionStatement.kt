package parser.syntaticMiniParsers.statementsType.miniParsers

import nodes.StatementType
import parser.syntaticMiniParsers.TokenManager
import parser.syntaticMiniParsers.expressions.miniParsers.ExpressionType
import parser.syntaticMiniParsers.statementsType.MiniStatementParser
import parser.syntaticMiniParsers.statementsType.ParseStatementResult
import token.Token

class ExpressionStatement(): MiniStatementParser {
    override fun parse(tokens: List<Token>): ParseStatementResult {
        val (remainingTokens, exp) = ExpressionType.makeExpressionEvaluator().parse(tokens)
        val manager = TokenManager(remainingTokens)
        val position = manager.getPosition()
        manager.consumeTokenValue(";")

        return Pair(manager.getTokens(), StatementType.StatementExpression(exp, position))
    }
}