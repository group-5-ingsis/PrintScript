package parser.syntactic.expressions

import token.Token

class ExpressionType(private val parseInferiorFunction: ExpressionParser) : ExpressionParser {

    override fun parse(tokens: List<Token>): ParseResult {
        return parseInferiorFunction.parse(tokens)
    }

    companion object {
        fun makeExpressionEvaluator(): ExpressionType {
            return ExpressionType(Assigment(Comparison(Term(Factor(Unary(Primary()))))))
        }
    }
}
