package parser.syntactic.statements

import nodes.Statement
import parser.syntactic.TokenManager
import parser.syntactic.expressions.ExpressionType
import token.Token

class ExpressionStatementParser(private val expressionEvaluator: ExpressionType) : StatementParser {
    override fun parse(tokens: List<Token>): ParseStatementResult {
        val (remainingTokens, exp) = expressionEvaluator.parse(tokens)
        val manager = TokenManager(remainingTokens)
        val position = manager.getPosition()
        manager.consumeTokenValue(";")

        return Pair(manager.getTokens(), Statement.StatementExpression(exp, position))
    }
}
