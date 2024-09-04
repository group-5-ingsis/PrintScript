package parser.syntactic.expressions

import nodes.Expression
import parser.syntactic.TokenManager
import token.Token

class Factor(private val parseInferiorFunction: ExpressionParser) : ExpressionParser {
    override fun parse(tokens: List<Token>): ParseResult {
        var (remainingTokens, expression) = parseInferiorFunction.parse(tokens)
        val tokenMng = TokenManager(remainingTokens)

        fun isMultiplicationOrDivision(): Boolean {
            return if (tokenMng.isNotTheEndOfTokens()) {
                (tokenMng.isValue("*") || tokenMng.isValue("/"))
            } else {
                false
            }
        }

        while (isMultiplicationOrDivision()) {
            val tokenOperator = tokenMng.peek().value
            val position = tokenMng.getPosition()
            tokenMng.advance()

            val (remainingTokens2, rightExpression) = parseInferiorFunction.parse(tokenMng.getTokens())
            remainingTokens = remainingTokens2
            expression = Expression.Binary(expression, tokenOperator, rightExpression, position)
        }
        return Pair(remainingTokens, expression)
    }
}
