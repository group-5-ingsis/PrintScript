package parser.syntactic.expressions.miniParsers

import nodes.Expression
import parser.syntactic.TokenManager
import parser.syntactic.expressions.MiniExpressionParser
import parser.syntactic.expressions.ParseResult
import token.Token

class Term(private val parseInferiorFunction: MiniExpressionParser) : MiniExpressionParser {

    override fun parse(tokens: List<Token>): ParseResult {
        var (remainingTokens, expression) = parseInferiorFunction.parse(tokens)

        var tokenMng = TokenManager(remainingTokens)

        fun isMinusOrPlus(): Boolean {
            return if (tokenMng.isNotTheEndOfTokens()) {
                (tokenMng.isValue("+") || tokenMng.isValue("-"))
            } else {
                false
            }
        }

        while (isMinusOrPlus()) {
            val tokenOperator = tokenMng.peek().value
            val position = tokenMng.getPosition()
            tokenMng.advance()

            val (remainingTokens2, rightExpression) = parseInferiorFunction.parse(tokenMng.getTokens())
            remainingTokens = remainingTokens2
            tokenMng = TokenManager(remainingTokens2)
            expression = Expression.Binary(expression, tokenOperator, rightExpression, position)
        }

        return Pair(remainingTokens, expression)
    }
}
