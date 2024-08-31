package parser.syntaticMiniParsers.expressions
import nodes.Expression
import token.Token


typealias ParseResult = Pair<List<Token>, Expression>

interface MiniExpressionParser {

    fun parse(tokens: List<Token>): ParseResult

}