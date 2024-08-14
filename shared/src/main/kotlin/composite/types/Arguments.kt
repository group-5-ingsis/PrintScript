package composite.types

import composite.Node
import composite.ResultType
import visitor.NodeResult
import visitor.NodeVisitor

class Arguments(private val values: List<Node>) : Node {

  override fun solve(): NodeResult {
    for (argument in values) {
      argument.solve()
    }
    return NodeResult(ResultType.ARGUMENTS, values, null)
  }

  override fun accept(visitor: NodeVisitor) {
    TODO("Not yet implemented")
  }
}