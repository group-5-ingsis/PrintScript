package parser.syntactic.expressions

import exception.UnknownExpressionException
import nodes.Expression
import parser.syntactic.TokenManager

class VariableParser : ExpressionParser {
    override fun parse(manager: TokenManager): Expression {
        val tokenManger = manager.advance()
        val token = tokenManger.peek()

        if (token.type != "IDENTIFIER") {
            throw UnknownExpressionException(token)
        }

        val position = manager.getPosition()
        return Expression.Variable(token.value, position)
    }
}
