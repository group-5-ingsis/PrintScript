package parser.syntactic.expressions
import nodes.Expression
import position.nodes.Type
import token.Token

typealias ParseResult = Pair<List<Token>, Expression>

interface ExpressionParser {

    fun parse(tokens: List<Token>, parsedShouldBeOfType: Type = Type.ANY): ParseResult
}
