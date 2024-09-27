package parser.syntactic.expressions

import exception.InvalidSyntaxException
import nodes.Expression
import parser.syntactic.TokenManager

class AssignmentParser(private val expressionParser: ExpressionParser) : ExpressionParser {
    override fun parse(manager: TokenManager): Expression {
        val expression = expressionParser.parse(manager)

        if (manager.isValue("=")) {
            manager.consumeValue("=")
            if (expression is Expression.Variable) {
                val value = expressionParser.parse(manager)
                return Expression.Assign(expression.name, value, manager.getPosition())
            } else {
                throw InvalidSyntaxException("Invalid assignment target.")
            }
        }
        return expression
    }
}
