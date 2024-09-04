package parser.syntactic.statementsType.miniParsers

import nodes.StatementType
import parser.syntactic.TokenManager
import parser.syntactic.expressions.miniParsers.ExpressionType
import parser.syntactic.statementsType.MiniStatementParser
import parser.syntactic.statementsType.ParseStatementResult
import token.Token

class ExpressionStatement() : MiniStatementParser {
    override fun parse(tokens: List<Token>): ParseStatementResult {
        val (remainingTokens, exp) = ExpressionType.makeExpressionEvaluator().parse(tokens)
        val manager = TokenManager(remainingTokens)
        val position = manager.getPosition()
        manager.consumeTokenValue(";")

        return Pair(manager.getTokens(), StatementType.StatementExpression(exp, position))
    }
}
