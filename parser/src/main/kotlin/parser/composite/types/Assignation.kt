package parser.composite.types

import interpreter.NodeVisitor
import parser.NodeResult
import parser.composite.Node

class Assignation(private val left: Identifier, private val right: Node) : Node {

  override fun solve() : NodeResult {
    val identifier = left.solve()
    val value = right.solve()

    if (value.type == ResultType.LITERAL) {
      return right.solve()
    }

    return NodeResult(ResultType.ASSIGNATION, declaration.primaryValue, value.primaryValue)
  }

  override fun accept(visitor: NodeVisitor): NodeResult {
    return visitor.visitAssignation(this)
  }

  fun getVariableName(): String{
    return left.getName()
  }

}