package parser.syntaticMiniParsers.expressions.miniParsers

import nodes.Expression
import parser.syntaticMiniParsers.TokenManager
import parser.syntaticMiniParsers.expressions.MiniExpressionParser
import parser.syntaticMiniParsers.expressions.ParseResult
import token.Token

class Unary(private val parseInferiorFunction: MiniExpressionParser) : MiniExpressionParser {

    override fun parse(tokens: List<Token>): ParseResult {
        val tokenManager = TokenManager(tokens)

        if (tokenManager.isValue("!")) {
            val tokenOperator = tokenManager.peek().value
            val position = tokenManager.getPosition()
            tokenManager.advance()
            val (remainingTokens2, rightExpression) = Unary(parseInferiorFunction).parse(tokenManager.getTokens())
            return Pair(remainingTokens2, Expression.Unary(tokenOperator, rightExpression, position))
        }
        if (tokenManager.peek().value == "-") {
            val position = tokenManager.getPosition()
            val tokenOperator = tokenManager.peek().value
            tokenManager.advance()
            val (remainingTokens2, rightExpression) = Unary(parseInferiorFunction).parse(tokenManager.getTokens())
            return Pair(remainingTokens2, Expression.Unary(tokenOperator, rightExpression, position))
        }

        return parseInferiorFunction.parse(tokens)
    }
}
