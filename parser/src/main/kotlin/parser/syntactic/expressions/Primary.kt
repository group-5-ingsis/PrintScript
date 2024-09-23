package parser.syntactic.expressions

import exception.UnknownExpressionException
import nodes.Expression
import parser.syntactic.TokenManager
import position.nodes.Type
import token.Token

class Primary(val version: String) : ExpressionParser {

    override fun parse(tokens: List<Token>, parsedShouldBeOfType: Type): ParseResult {
        val tokenMng = TokenManager(tokens)
        val position = tokenMng.getPosition()

        fun getNextLiteral(): Any {
            val next = tokenMng.advance()
            return when (next.type) {
                "STRING" -> next.value
                else -> {
                    if (next.value.contains('.')) next.value.toDouble() else next.value.toInt()
                }
            }
        }

        fun parseBoolean(): ParseResult {
            val token = tokenMng.advance()
            return tokenMng.getTokens() to Expression.Literal(token.value.toBoolean(), position)
        }

        fun parseReadEnv(): ParseResult {
            tokenMng.advance()
            val evaluator = ExpressionType.makeExpressionEvaluatorV1_1()
            val expr = evaluator.parse(tokenMng.getTokens())
            return expr.first to Expression.ReadEnv(position, expr.second as Expression.Grouping)
        }

        fun parseLiteral(): ParseResult {
            val nextLiteral = getNextLiteral()
            return tokenMng.getTokens() to Expression.Literal(nextLiteral, position)
        }

         fun parseReadInput(): ParseResult {
             tokenMng.advance()
             val result = Primary("1.1").parse(tokenMng.getTokens())
             val tokenMng2 = TokenManager(result.first)
             val grouping = result.second as Expression.Grouping
             return Pair(tokenMng2.getTokens(), Expression.ReadInput(tokenMng.getPosition(), grouping, parsedShouldBeOfType))
        }

        fun parseGrouping(): ParseResult {
            tokenMng.advance()
            val evaluator = if (version == "1.1") {
                ExpressionType.makeExpressionEvaluatorV1_1()
            } else {
                ExpressionType.makeExpressionEvaluatorV1_0()
            }
            val expr = evaluator.parse(tokenMng.getTokens())
            val newTokenMng = TokenManager(expr.first)
            newTokenMng.consumeTokenValue(")")
            return newTokenMng.getTokens() to Expression.Grouping(expr.second, position)
        }

        return when {
            version == "1.1" && tokenMng.nextTokenMatchesExpectedType("BOOLEAN") -> parseBoolean()
            version == "1.1" && tokenMng.nextTokenMatchesExpectedType("READ_ENV") -> parseReadEnv()
            version === "1.1" && tokenMng.nextTokenMatchesExpectedType("READ_INPUT") -> parseReadInput()
            tokenMng.nextTokenMatchesExpectedType("NULL") -> {
                tokenMng.advance()
                tokenMng.getTokens() to Expression.Literal(null, position)
            }
            tokenMng.checkTokensAreFromSomeTypes(listOf("NUMBER", "STRING")) -> parseLiteral()
            tokenMng.peek().value == "(" -> parseGrouping()
            tokenMng.nextTokenMatchesExpectedType("IDENTIFIER") -> {
                val identifier = tokenMng.advance().value
                tokenMng.getTokens() to Expression.Variable(identifier, position)
            }
            else -> throw UnknownExpressionException(tokenMng.peek())
        }
    }
}
