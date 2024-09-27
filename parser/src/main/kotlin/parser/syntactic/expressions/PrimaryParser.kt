package parser.syntactic.expressions

import exception.UnknownExpressionException
import nodes.Expression
import parser.syntactic.TokenManager

class PrimaryParser(private val version: String) : ExpressionParser {
    override fun parse(manager: TokenManager): Expression {
        return when {
            manager.peek().type == "NUMBER" || manager.peek().type == "STRING" -> LiteralParser().parse(manager)
            manager.peek().value == "(" -> GroupingParser(PrimaryParser(version)).parse(manager)
            manager.peek().type == "IDENTIFIER" -> VariableParser().parse(manager)
            else -> throw UnknownExpressionException(manager.peek())
        }
    }
}
