package composite.types

import composite.Node
import visitor.NodeResult
import visitor.NodeVisitor

class Assignation(private val left: Identifier, private val right: Node) : Node {

  override fun solve() : NodeResult {

    val identifierInfo = left.solve()
    val literalInfo = right.solve()

    val identifier = identifierInfo.primaryValue
    val value = literalInfo.primaryValue

    return NodeResult(ResultType.ASSIGNATION, identifier, value)
  }

  override fun accept(visitor: NodeVisitor) {
    visitor.visitAssignation(this)
  }

}