package parser.syntactic.expressions

import nodes.Expression
import parser.syntactic.ParserFactory
import parser.syntactic.TokenManager

class GroupingParser(val version: String) : ExpressionParser {
  override fun parse(manager: TokenManager): Expression {
    val expressionParser = ParserFactory.createExpressionParser(manager, version)
    manager.consumeValue("(")
    val expr = expressionParser.parse(manager)
    manager.consumeValue(")")
    return Expression.Grouping(expr, manager.getPosition())
  }
}
