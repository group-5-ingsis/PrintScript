package parser.syntactic.expressions
import nodes.Expression
import parser.syntactic.TokenManager

interface ExpressionParser {
  fun parse(manager: TokenManager): Expression
}
