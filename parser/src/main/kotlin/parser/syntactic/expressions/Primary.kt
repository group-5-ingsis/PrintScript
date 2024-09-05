package parser.syntactic.expressions

import exception.UnknownExpressionException
import nodes.Expression
import parser.syntactic.TokenManager
import token.Token

class Primary() : ExpressionParser {

    override fun parse(tokens: List<Token>): ParseResult {
        val tokenMng = TokenManager(tokens)

        fun getNextLiteral(): Any {
            val next = tokenMng.advance()
            return if (next.type == "STRING") {
                next.value
            } else {
                if (next.value.contains('.')) {
                    next.value.toDouble()
                } else {
                    next.value.toInt()
                }
            }
        }

        val position = tokenMng.getPosition()
        if (tokenMng.checkNextTokenType("BOOLEAN")) {
            val token = tokenMng.advance()
            return Pair(tokenMng.getTokens(), Expression.Literal(token.value.toBoolean(), position))
        } else if (tokenMng.checkNextTokenType("NULL")) {
            tokenMng.advance()
            return Pair(tokenMng.getTokens(), Expression.Literal(null, position))
        } else if (tokenMng.checkTokensAreFromSomeTypes(listOf("NUMBER", "STRING"))) {
            val nextLiteral = getNextLiteral()
            return Pair(tokenMng.getTokens(), Expression.Literal(nextLiteral, position))
        } else if (tokenMng.peek().value == "(") {
            tokenMng.advance()
            val expr = ExpressionType(Assigment(Comparison(Term(Factor(Unary(Primary())))))).parse(tokenMng.getTokens())
            val newTK = parser.syntactic.TokenManager(expr.first)
            newTK.consumeTokenValue(")")
            return Pair(newTK.getTokens(), Expression.Grouping(expr.second, position))
        } else if (tokenMng.checkNextTokenType("IDENTIFIER")) {
            val idem = tokenMng.advance().value
            return Pair(tokenMng.getTokens(), Expression.Variable(idem, position))
        }

        throw UnknownExpressionException(tokenMng.peek())
    }
}
