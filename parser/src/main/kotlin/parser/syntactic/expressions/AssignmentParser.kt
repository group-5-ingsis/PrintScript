package parser.syntactic.expressions

import exception.InvalidSyntaxException
import nodes.Expression
import parser.syntactic.ParserFactory
import parser.syntactic.TokenManager

class AssignmentParser(private val version: String) : ExpressionParser {
    override fun parse(manager: TokenManager): Expression {
        val expressionParser = ParserFactory.createExpressionParser(manager, version)
        val expression = expressionParser.parse(manager)

        if (manager.isValue("=")) {
            val position = manager.getPosition()
            val newManager = manager.consumeValue("=")

            if (expression is Expression.Variable) {
                val value = expressionParser.parse(newManager)
                return Expression.Assign(expression.name, value, position)
            } else {
                throw InvalidSyntaxException("Invalid assignment target at line: ${position.line}, column: ${position.symbolIndex}")
            }
        }
        return expression
    }
}
