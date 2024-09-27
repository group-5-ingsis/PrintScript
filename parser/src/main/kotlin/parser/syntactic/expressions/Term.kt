package parser.syntactic.expressions

import nodes.Expression
import parser.syntactic.TokenManager
import token.Token

class Term(private val parseInferiorFunction: ExpressionParser) : ExpressionParser {

    override fun parse(tokens: List<Token>, parsedShouldBeOfType: Type): ParseResult {
        var (remainingTokens, expression) = parseInferiorFunction.parse(tokens, parsedShouldBeOfType)

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

            val (remainingTokens2, rightExpression) = parseInferiorFunction.parse(tokenMng.getTokens(), parsedShouldBeOfType)
            remainingTokens = remainingTokens2
            tokenMng = TokenManager(remainingTokens2)
            expression = Expression.Binary(expression, tokenOperator, rightExpression, position)
        }

        return Pair(remainingTokens, expression)
    }
}
