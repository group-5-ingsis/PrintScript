package parser.syntactic.expressions

import exception.InvalidSyntaxException
import nodes.Expression
import parser.syntactic.ParserFactory
import parser.syntactic.TokenManager

class AssignmentParser(val version: String) : ExpressionParser {
    override fun parse(manager: TokenManager): Expression {
        var tokenManager = manager.advance()
        tokenManager = manager.consumeValue("=")

        val expressionParser = ParserFactory.createExpressionParser(tokenManager, version)
        val expression = expressionParser.parse(tokenManager)

        if (expression is Expression.Variable) {
            val value = expressionParser.parse(manager)
            return Expression.Assign(expression.name, value, manager.getPosition())
        } else {
            throw InvalidSyntaxException("Invalid assignment target.")
        }
    }
}
