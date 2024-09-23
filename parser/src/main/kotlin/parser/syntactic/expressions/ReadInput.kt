package parser.syntactic.expressions

import nodes.Expression
import parser.syntactic.TokenManager
import position.nodes.Type
import token.Token

class ReadInput(private val shouldBeOfType: Type, private val parseInferiorFunction: ExpressionParser) : ExpressionParser {

    override fun parse(tokens: List<Token>): ParseResult {
        val result = parseInferiorFunction.parse(tokens)
        val tokenMng = TokenManager(result.first)
        val grouping = result.second as Expression.Grouping
        return Pair(tokenMng.getTokens(), Expression.ReadInput(tokenMng.getPosition(), grouping, shouldBeOfType))
    }
}
