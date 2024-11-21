package parser.syntactic.expressions

import nodes.Expression
import parser.syntactic.TokenManager
import position.nodes.Type
import token.Token

class Unary(private val parseInferiorFunction: ExpressionParser) : ExpressionParser {

  override fun parse(tokens: List<Token>, parsedShouldBeOfType: Type): ParseResult {
    val tokenManager = TokenManager(tokens)

    if (tokenManager.isValue("!") && tokenManager.isType("OPERATOR")) {
      val tokenOperator = tokenManager.peek().value
      val position = tokenManager.getPosition()
      tokenManager.advance()
      val (remainingTokens2, rightExpression) = Unary(parseInferiorFunction).parse(tokenManager.getTokens(), parsedShouldBeOfType)
      return Pair(remainingTokens2, Expression.Unary(tokenOperator, rightExpression, position))
    }
    if (tokenManager.peek().value == "-") {
      val position = tokenManager.getPosition()
      val tokenOperator = tokenManager.peek().value
      tokenManager.advance()
      val (remainingTokens2, rightExpression) = Unary(parseInferiorFunction).parse(tokenManager.getTokens(), parsedShouldBeOfType)
      return Pair(remainingTokens2, Expression.Unary(tokenOperator, rightExpression, position))
    }

    return parseInferiorFunction.parse(tokens, parsedShouldBeOfType)
  }
}
