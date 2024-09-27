package parser.syntactic.expressions

import nodes.Expression
import parser.syntactic.TokenManager

class MethodCallParser(private val expressionParser: ExpressionParser) : ExpressionParser {
    override fun parse(manager: TokenManager): Expression {
        val callee = expressionParser.parse(manager)
        manager.consumeValue("(")
        val arguments = mutableListOf<Expression>()

        if (!manager.isValue(")")) {
            do {
                arguments.add(expressionParser.parse(manager))
            } while (manager.isValue(","))
        }

        manager.consumeValue(")")
        return Expression.Call(callee, arguments, manager.getPosition())
    }
}
