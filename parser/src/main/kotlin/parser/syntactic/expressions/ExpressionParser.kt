package parser.syntactic.expressions
import nodes.Expression
import token.Token

interface ExpressionParser {
    fun parse(tokens: List<Token>): Expression
}
