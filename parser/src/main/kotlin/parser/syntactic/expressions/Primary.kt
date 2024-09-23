package parser.syntactic.expressions

import exception.UnknownExpressionException
import nodes.Expression
import parser.syntactic.TokenManager
import token.Token

class Primary : ExpressionParser {

    override fun parse(tokens: List<Token>): ParseResult {
        val tokenMng = TokenManager(tokens)

        fun getNextLiteral(): Any {
            // Handles strings and numbers
            val next = tokenMng.advance()
            return when (next.type) {
                "STRING" -> next.value
                else -> {
                    if (next.value.contains('.')) {
                        next.value.toDouble()
                    } else {
                        next.value.toInt()
                    }
                }
            }
        }

        val position = tokenMng.getPosition()

        return when {
            tokenMng.nextTokenMatchesExpectedType("BOOLEAN") -> {
                val token = tokenMng.advance()
                Pair(tokenMng.getTokens(), Expression.Literal(token.value.toBoolean(), position))
            }
            tokenMng.nextTokenMatchesExpectedType("NULL") -> {
                tokenMng.advance()
                Pair(tokenMng.getTokens(), Expression.Literal(null, position))
            }
            tokenMng.checkTokensAreFromSomeTypes(listOf("NUMBER", "STRING")) -> {
                val nextLiteral = getNextLiteral()
                Pair(tokenMng.getTokens(), Expression.Literal(nextLiteral, position))
            }
            tokenMng.peek().value == "(" -> {
                tokenMng.advance()
                val expressionEvaluator = ExpressionType.makeExpressionEvaluator()
                val expr = expressionEvaluator.parse(tokenMng.getTokens())
                val newTK = TokenManager(expr.first)
                newTK.consumeTokenValue(")")
                Pair(newTK.getTokens(), Expression.Grouping(expr.second, position))
            }
            tokenMng.nextTokenMatchesExpectedType("IDENTIFIER") -> {
                val idem = tokenMng.advance().value
                Pair(tokenMng.getTokens(), Expression.Variable(idem, position))
            }
            tokenMng.nextTokenMatchesExpectedType("READ_INPUT") -> {
                tokenMng.advance()
                val expressionEvaluator = ExpressionType.makeExpressionEvaluator()
                val expr = expressionEvaluator.parse(tokenMng.getTokens())
                val message = "Default message"
                Pair(expr.first, Expression.ReadInput(position, expr.second as Expression.Grouping, message))
            }
            tokenMng.nextTokenMatchesExpectedType("READ_ENV") -> {
                tokenMng.advance()
                val expressionEvaluator = ExpressionType.makeExpressionEvaluator()
                val expr = expressionEvaluator.parse(tokenMng.getTokens())
                Pair(expr.first, Expression.ReadEnv(position, expr.second as Expression.Grouping))
            }
            else -> throw UnknownExpressionException(tokenMng.peek())
        }
    }
}
