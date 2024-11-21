
import parser.syntactic.expressions.*
import position.nodes.Type
import token.Token

class ExpressionType(private val parseInferiorFunction: ExpressionParser, val version: String = "1.1") {

  fun parse(tokens: List<Token>, parsedShouldBeOfType: Type = Type.ANY): ParseResult {
    return parseInferiorFunction.parse(tokens, parsedShouldBeOfType)
  }

  companion object {
    fun makeExpressionEvaluatorV1_0(): ExpressionType {
      return ExpressionType(Assigment(Term(Factor(Unary(Primary("1.0"))))), "1.0")
    }
    fun makeExpressionEvaluatorV1_1(): ExpressionType {
      return ExpressionType(Assigment(Term(Factor(Unary(Primary("1.1"))))), "1.1")
    }
  }
}
