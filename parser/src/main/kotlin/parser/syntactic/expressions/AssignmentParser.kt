package parser.syntactic.expressions

import exception.InvalidSyntaxException
import nodes.Expression
import parser.syntactic.TokenManager

object AssignmentParser : ExpressionParser {
    override fun parse(manager: TokenManager): Expression {
        val expression = parsePrimaryExpression(manager)

        if (manager.isValue("=")) {
            val position = manager.getPosition()
            manager.consume("=")

            if (expression is Expression.Variable) {
                val value = parsePrimaryExpression(manager)
                return Expression.Assign(expression.name, value, position)
            } else {
                throw InvalidSyntaxException("Invalid assignment target at line: ${position.line}, column: ${position.symbolIndex}")
            }
        }
        return expression
    }

    private fun parsePrimaryExpression(manager: TokenManager): Expression {
        val token = manager.peek()
        return when (token.type) {
            "IDENTIFIER" -> Expression.Variable(token.value, token.position)
            "NUMBER" -> Expression.Literal(token.value.toDouble(), token.position)
            else -> throw InvalidSyntaxException("Unexpected token: ${token.value} at line: ${token.position.line}")
        }
    }
}
