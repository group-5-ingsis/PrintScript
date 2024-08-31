package parser.syntaticMiniParsers.expressions.miniParsers


import exceptions.BadSyntacticException
import nodes.Expression
import parser.syntaticMiniParsers.expressions.MiniExpressionParser
import parser.syntaticMiniParsers.expressions.ParseResult
import parser.syntaticMiniParsers.TokenManager
import token.Token

class Assigment(private val parseInferiorFunction: MiniExpressionParser): MiniExpressionParser {

    override fun parse(tokens: List<Token>): ParseResult {

        val (remainingTokens, expression) =  parseInferiorFunction.parse(tokens)

        val tokenManager = TokenManager(remainingTokens)

        if (tokenManager.isValue("=")){

            val position =  tokenManager.getPosition()

            tokenManager.consumeTokenValue("=")

            if (expression is Expression.Variable){
                // use case example ->  newPoint(x + 2, 0).y = 3;
                // this should be a Variable:  newPoint(x + 2, 0).y

                val (newTokens, exp) = ExpressionType.makeExpressionEvaluator().parse(tokenManager.getTokens())

                return Pair(newTokens, Expression.Assign(expression.name, exp, position))
            }
            throw BadSyntacticException("Invalid assignment target. at: " + position.line + " line, and: " + position.symbolIndex + "column")

        }
        return Pair(remainingTokens, expression)
    }


}