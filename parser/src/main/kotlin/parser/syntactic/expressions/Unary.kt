package parser.syntactic.expressions

import nodes.Expression
import nodes.Type
import parser.syntactic.TokenManager
import token.Token

class Unary(private val parseInferiorFunction: ExpressionParser) : ExpressionParser {

  override fun parse(tokens: List<Token>, parsedShouldBeOfType: Type): ParseResult {
    val tokenManager = TokenManager(tokens)

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
