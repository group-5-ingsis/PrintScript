package parser.syntactic.expressions

import nodes.Expression
import parser.syntactic.TokenManager

class GroupingParser(private val expressionParser: ExpressionParser) : ExpressionParser {
    override fun parse(manager: TokenManager): Expression {
        manager.consumeValue("(")
        val expr = expressionParser.parse(manager)
        manager.consumeValue(")")
        return Expression.Grouping(expr, manager.getPosition())
    }
}
