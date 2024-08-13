package composite.types

import composite.Node
import composite.ResultType
import visitor.NodeResult
import visitor.NodeVisitor

class StringLiteral(private val value: String) : Node {

  override fun solve(): NodeResult {
    return NodeResult(ResultType.LITERAL, value, null)
  }

  override fun accept(visitor: NodeVisitor) {
    TODO("Not yet implemented")
  }

}