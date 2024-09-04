package parser.syntactic.expressions

import nodes.Expression
import parser.syntactic.TokenManager
import token.Token

class Comparison(private val parseInferiorFunction: ExpressionParser) : ExpressionParser {

    override fun parse(tokens: List<Token>): ParseResult {
        var (remainingTokens, expression) = parseInferiorFunction.parse(tokens)
        val tokenManager = TokenManager(remainingTokens)

        while (tokenManager.checkNextTokenType("COMPARISON")) {
            val position = tokenManager.getPosition()
            val tokenOperator = tokenManager.peek().value

            tokenManager.advance()
            val (remainingTokens2, rightExpression) = parseInferiorFunction.parse(tokens)
            remainingTokens = remainingTokens2

            expression = Expression.Binary(expression, tokenOperator, rightExpression, position)
        }
        return Pair(remainingTokens, expression)
    }
}
