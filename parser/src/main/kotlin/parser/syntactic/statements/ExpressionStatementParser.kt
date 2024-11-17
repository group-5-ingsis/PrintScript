package parser.syntactic.statements

import nodes.Statement
import parser.syntactic.ParserFactory
import parser.syntactic.TokenManager

class ExpressionStatementParser(val version: String) : StatementParser {
  override fun parse(manager: TokenManager): Statement {
    val expressionParser = ParserFactory.createExpressionParser(manager, version)
    val expression = expressionParser.parse(manager)
    var tokenManager = manager.consumeValue(";")
    val position = tokenManager.getPosition()

    return Statement.StatementExpression(expression, position)
  }
}
