package parser.syntactic.expressions

import token.Token

class ExpressionType(private val parseInferiorFunction: ExpressionParser) : ExpressionParser {

    override fun parse(tokens: List<Token>): ParseResult {
        return parseInferiorFunction.parse(tokens)
    }

    companion object {
        fun makeExpressionEvaluatorV1_0(): ExpressionType {
            return ExpressionType(Assigment(Comparison(Term(Factor(Unary(PrimaryV1()))))))
        }
        fun makeExpressionEvaluatorV1_1(): ExpressionType {
            return ExpressionType(Assigment(Comparison(Term(Factor(Unary(PrimaryV1_1()))))))
        }

    }
}
