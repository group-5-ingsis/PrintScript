package parser.syntactic.expressions

import exception.UnknownExpressionException
import nodes.Expression
import parser.syntactic.TokenManager

class LiteralParser : ExpressionParser {
    override fun parse(manager: TokenManager): Expression {
        val token = manager.peek()
        val value = when (token.type) {
            "NUMBER" -> token.value.toDouble()
            "STRING" -> token.value
            "TRUE" -> true
            "FALSE" -> false
            "NULL" -> null
            else -> throw UnknownExpressionException(token)
        }
        return Expression.Literal(value, manager.getPosition())
    }
}
