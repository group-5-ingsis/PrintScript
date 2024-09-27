package parser.syntactic.expressions

import exception.UnknownExpressionException
import nodes.Expression
import parser.syntactic.TokenManager

class VariableParser : ExpressionParser {
    override fun parse(manager: TokenManager): Expression {
        val identifier = manager.advance()
        if (identifier.type != "IDENTIFIER") {
            throw UnknownExpressionException(identifier)
        }
        return Expression.Variable(identifier.value, manager.getPosition())
    }
}
