package composite.types

import composite.Node
import visitor.NodeResult
import visitor.NodeVisitor

class Literal(private val value : String) : Node {
  override fun solve(): NodeResult {
    return NodeResult(ResultType.LITERAL, value, null)
  }

  override fun accept(visitor: NodeVisitor): NodeResult {
    TODO("Not yet implemented")
  }
}