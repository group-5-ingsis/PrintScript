package parser.syntactic.expressions.miniParsers

import parser.syntactic.expressions.MiniExpressionParser
import parser.syntactic.expressions.ParseResult
import token.Token

class ExpressionType(private val parseInferiorFunction: MiniExpressionParser) : MiniExpressionParser {

    override fun parse(tokens: List<Token>): ParseResult {
        return parseInferiorFunction.parse(tokens)
    }

    companion object {
        fun makeExpressionEvaluator(): ExpressionType {
            return ExpressionType(Assigment(Comparison(Term(Factor(Unary(Primary()))))))
        }
    }
}
