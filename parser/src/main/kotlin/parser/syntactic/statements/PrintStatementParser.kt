package parser.syntactic.statements

import exception.InvalidSyntaxException
import nodes.Expression
import nodes.Statement
import parser.syntactic.TokenManager

object PrintStatementParser : StatementParser {
    override fun parse(manager: TokenManager): Statement {
        val position = manager.getPosition()
        val expression = parseExpression(manager)
        manager.consume(";")
        if (expression is Expression.Grouping) {
            return Statement.Print(expression, position)
        } else {
            throw Error("Expected a grouping expression at line: ${position.line}")
        }
    }

    private fun parseExpression(manager: TokenManager): Expression {
        val token = manager.advance()
        return when (token.type) {
            "STRING" -> Expression.Literal(token.value, token.position)
            "NUMBER" -> Expression.Literal(token.value.toDouble(), token.position)
            else -> throw InvalidSyntaxException("Invalid expression at line: ${token.position.line}")
        }
    }
}
