package nodes

import visitor.NodeVisitor

sealed class Expression {
  abstract val expressionType: String

  fun acceptVisitor(visitor: NodeVisitor) {
    val func = visitor.getVisitorFunctionForExpression(expressionType)
    func(this)
  }

  data class Binary(val left: Expression, val operator: String, val right: Expression) : Expression() {
    override val expressionType: String = "BINARY_EXPRESSION"
  }

  data class Grouping(val expression: Expression) : Expression(){
    override val expressionType: String = "GROUPING_EXPRESSION"
  }

  data class Literal(val value: Any?) : Expression(){
    override val expressionType: String = "LITERAL_EXPRESSION"

  }

  data class Unary(val operator: String, val right: Expression) : Expression(){
    override val expressionType: String = "UNARY_EXPRESSION"
  }

  data class IdentifierExpression(val name: String) : Expression(){
    override val expressionType: String = "IDENTIFIER"
  }

}
