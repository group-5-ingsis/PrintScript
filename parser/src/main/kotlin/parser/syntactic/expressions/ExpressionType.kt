package parser.syntactic.expressions

import parser.syntactic.TokenManager
import position.nodes.Type
import token.Token

class ExpressionType(private val parseInferiorFunction: ExpressionParser, val version: String = "1.1") {

    fun parse(tokens: List<Token>, parsedShouldBeOfType: Type = Type.ANY): ParseResult {
        if (version == "1.0") {
            return parseInferiorFunction.parse(tokens)
        }

        val manager = TokenManager(tokens)

        if (manager.isType("READ_INPUT")) {
            manager.advance()
            return ReadInput(parsedShouldBeOfType, parseInferiorFunction).parse(manager.getTokens())
        }

        return parseInferiorFunction.parse(tokens)
    }

    companion object {

        fun makeExpressionEvaluator(): ExpressionType {
            return ExpressionType(Assigment(Term(Factor(Unary(Primary())))))
        }
    }
}
