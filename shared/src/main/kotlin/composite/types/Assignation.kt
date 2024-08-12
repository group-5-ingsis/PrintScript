package composite.types

import composite.Node
import visitor.NodeResult
import visitor.NodeVisitor

class Assignation(private val left: Identifier, private val right: Node) : Node {

  override fun solve() : NodeResult {
    val identifier = left.solve()
    val value = right.solve()

    if (value.type == ResultType.LITERAL) {
      return right.solve()
    }

    return NodeResult(ResultType.ASSIGNATION, identifier.primaryValue, value.primaryValue)
  }

  override fun accept(visitor: NodeVisitor) {
    visitor.visitAssignation(this)
  }

}