package parser.syntactic.statements

import nodes.Statement
import parser.syntactic.ParserFactory
import parser.syntactic.TokenManager
import token.Token

class ExpressionStatementParser(val version: String) : StatementParser {
  override fun parse(tokens: List<Token>): Statement {
    val manager = TokenManager(tokens)
    val expressionParser = ParserFactory.createExpressionParser(manager, version)
    val expression = expressionParser.parse(manager)
    var tokenManager = manager.consumeValue(";")
    val position = tokenManager.getPosition()

    return Statement.StatementExpression(expression, position)
  }
}
