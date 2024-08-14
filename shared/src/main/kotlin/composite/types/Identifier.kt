package composite.types

import composite.Node
import composite.ResultType
import visitor.NodeResult
import visitor.NodeVisitor

class Identifier(private val value: String) : Node {

  override fun solve(): NodeResult {
    return NodeResult(ResultType.IDENTIFIER, value, null)
  }

  fun getValue() : String {
    return value
  }

  override fun accept(visitor: NodeVisitor) {
    TODO("Not yet implemented")
  }

}