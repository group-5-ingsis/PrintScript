package parser.syntactic.expressions
import nodes.Expression
import token.Token

typealias ParseResult = Pair<List<Token>, Expression>

interface ExpressionParser {

    fun parse(tokens: List<Token>): ParseResult
}
