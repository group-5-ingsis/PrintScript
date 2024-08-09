package parser.composite.types

import interpreter.NodeVisitor
import parser.NodeResult
import parser.composite.Node

class Assignation(private val left: Node, private val right: Node) : Node {
  override fun solve() : NodeResult {
    val declaration = left.solve()
    val value = right.solve()

    if (declaration.type != ResultType.DECLARATION) {
      throw Exception("Expected a declared value, got ${declaration.type}")
    }
    if (value.type != ResultType.LITERAL) {
      throw Exception("Expected a value, got ${value.type}")
    }

    return NodeResult(ResultType.ASSIGNATION, declaration.primaryValue, value.primaryValue)
  }

  override fun accept(visitor: NodeVisitor): NodeResult {
    return visitor.visitAssignation(this)
  }
}