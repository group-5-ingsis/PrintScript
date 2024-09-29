package parser.syntactic.expressions

import exception.UnknownExpressionException
import nodes.Expression
import parser.syntactic.ParserFactory
import parser.syntactic.TokenManager

class IdentifierExpressionParser(private val version: String) : ExpressionParser {
    override fun parse(manager: TokenManager): Expression {
        val tokenManger = manager.advance()
        val token = tokenManger.peek()

        val tokenType = token.type
        if (tokenType != "ASSIGNMENT") {
            throw UnknownExpressionException(token)
        }

        val expressionParser = ParserFactory.createExpressionParser(tokenManger, version)
        val expression = expressionParser.parse(tokenManger)

        val position = manager.getPosition()
        return Expression.IdentifierExpression(token.value, position)
    }
}
