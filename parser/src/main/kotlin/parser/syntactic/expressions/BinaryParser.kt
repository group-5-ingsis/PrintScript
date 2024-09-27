package parser.syntactic.expressions

import nodes.Expression
import parser.syntactic.TokenManager

class BinaryParser(private val leftParser: ExpressionParser, private val operatorTypes: List<String>) : ExpressionParser {
    override fun parse(manager: TokenManager): Expression {
        var left = leftParser.parse(manager)

        while (manager.peek().type in operatorTypes) {
            val operator = manager.advance()
            val right = leftParser.parse(manager)
            left = Expression.Binary(left, operator, right, manager.getPosition())
        }
        return left
    }
}
