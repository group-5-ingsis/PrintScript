package parser.syntactic.expressions

import exception.UnknownExpressionException
import nodes.Expression
import parser.syntactic.TokenManager
import token.Token

class PrimaryV1_1() : ExpressionParser {

    override fun parse(tokens: List<Token>): ParseResult {
        val tokenMng = TokenManager(tokens)

        fun getNextLiteral(): Any {
            // Only works for strings and numbers
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

        if (tokenMng.nextTokenMatchesExpectedType("BOOLEAN")) {
            val token = tokenMng.advance()
            return Pair(tokenMng.getTokens(), Expression.Literal(token.value.toBoolean(), position))
        }

        if (tokenMng.nextTokenMatchesExpectedType("NULL")) {
            tokenMng.advance()
            return Pair(tokenMng.getTokens(), Expression.Literal(null, position))
        } else if (tokenMng.checkTokensAreFromSomeTypes(listOf("NUMBER", "STRING"))) {
            val nextLiteral = getNextLiteral()
            return Pair(tokenMng.getTokens(), Expression.Literal(nextLiteral, position))
        } else if (tokenMng.peek().value == "(") {
            tokenMng.advance()
            val expressionEvaluator = ExpressionType.makeExpressionEvaluatorV1_1()
            val expr = expressionEvaluator.parse(tokenMng.getTokens())
            val newTK = TokenManager(expr.first)
            newTK.consumeTokenValue(")")
            return Pair(newTK.getTokens(), Expression.Grouping(expr.second, position))
        } else if (tokenMng.nextTokenMatchesExpectedType("IDENTIFIER")) {
            val idem = tokenMng.advance().value
            return Pair(tokenMng.getTokens(), Expression.Variable(idem, position))
        } else if (tokenMng.nextTokenMatchesExpectedType("READ_INPUT")) {
            tokenMng.advance()
            val expressionEvaluator = ExpressionType.makeExpressionEvaluatorV1_1()
            val expr = expressionEvaluator.parse(tokenMng.getTokens())
            val message = "Default message"
            return Pair(expr.first, Expression.ReadInput(position, expr.second as Expression.Grouping, message))
        } else if (tokenMng.nextTokenMatchesExpectedType("READ_ENV")) {
            tokenMng.advance()
            val expressionEvaluator = ExpressionType.makeExpressionEvaluatorV1_1()
            val expr = expressionEvaluator.parse(tokenMng.getTokens())

            return Pair(expr.first, Expression.ReadEnv(position, expr.second as Expression.Grouping))
        }

        throw UnknownExpressionException(tokenMng.peek())
    }
}
