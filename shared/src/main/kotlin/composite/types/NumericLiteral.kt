package composite.types

import composite.Node
import visitor.NodeResult
import visitor.NodeVisitor

class NumericLiteral(private val value: Number) : Node {

  override fun solve(): NodeResult {
    return NodeResult(ResultType.LITERAL, value, null)
  }

  override fun accept(visitor: NodeVisitor) {
    TODO("Not yet implemented")
  }

}