package parser.syntactic.expressions

import exception.InvalidSyntaxException
import nodes.Expression
import nodes.Type
import parser.syntactic.TokenManager
import token.Token

class Assigment(private val parseInferiorFunction: ExpressionParser) : ExpressionParser {

    override fun parse(tokens: List<Token>, parsedShouldBeOfType: Type): ParseResult {
        val (remainingTokens, expression) = parseInferiorFunction.parse(tokens, parsedShouldBeOfType)

        val tokenManager = TokenManager(remainingTokens)

        if (tokenManager.isValue("=")) {
            val position = tokenManager.getPosition()

            tokenManager.consumeTokenValue("=")

            if (expression is Expression.Variable) {
                // use case example ->  newPoint(x + 2, 0).y = 3;
                // this should be a Variable:  newPoint(x + 2, 0).y

                val (newTokens, exp) = parseInferiorFunction.parse(tokenManager.getTokens(), parsedShouldBeOfType)

                return Pair(newTokens, Expression.Assign(expression.name, exp, position))
            }
            throw InvalidSyntaxException("Invalid assignment target. at: " + position.line + " line, and: " + position.symbolIndex + "column")
        }
        return Pair(remainingTokens, expression)
    }
}
