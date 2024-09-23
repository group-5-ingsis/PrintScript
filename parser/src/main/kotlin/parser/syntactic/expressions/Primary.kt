package parser.syntactic.expressions

import exception.UnknownExpressionException
import nodes.Expression
import parser.syntactic.TokenManager
import token.Token

class Primary(val version: String) : ExpressionParser {

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
        if (version == "1.1") {

            if (tokenMng.nextTokenMatchesExpectedType("BOOLEAN")) {
                val token = tokenMng.advance()
                return Pair(tokenMng.getTokens(), Expression.Literal(token.value.toBoolean(), position))
            } else if (tokenMng.nextTokenMatchesExpectedType("READ_ENV")) {
                tokenMng.advance()
                val expressionEvaluator = ExpressionType.makeExpressionEvaluatorV1_1()
                val expr = expressionEvaluator.parse(tokenMng.getTokens())
                return Pair(expr.first, Expression.ReadEnv(position, expr.second as Expression.Grouping))
            } else if (tokenMng.isType("READ_INPUT")) {
                val expressionEvaluator = ExpressionType.makeExpressionEvaluatorV1_1()

                return expressionEvaluator.parse(tokenMng.getTokens())
            }

        }


        if (tokenMng.nextTokenMatchesExpectedType("NULL")) {
            tokenMng.advance()
            return Pair(tokenMng.getTokens(), Expression.Literal(null, position))
        } else if (tokenMng.checkTokensAreFromSomeTypes(listOf("NUMBER", "STRING"))) {
            val nextLiteral = getNextLiteral()
            return Pair(tokenMng.getTokens(), Expression.Literal(nextLiteral, position))
        } else if (tokenMng.peek().value == "(") {
            tokenMng.advance()

            val expressionEvaluator = if (version == "1.1") {
                ExpressionType.makeExpressionEvaluatorV1_1()
            } else {
                ExpressionType.makeExpressionEvaluatorV1_0()
            }

            val expr = expressionEvaluator.parse(tokenMng.getTokens())
            val newTK = TokenManager(expr.first)
            newTK.consumeTokenValue(")")
            return Pair(newTK.getTokens(), Expression.Grouping(expr.second, position))
        } else if (tokenMng.nextTokenMatchesExpectedType("IDENTIFIER")) {
            val idem = tokenMng.advance().value
            return Pair(tokenMng.getTokens(), Expression.Variable(idem, position))

        }

        throw UnknownExpressionException(tokenMng.peek())
    }
}
