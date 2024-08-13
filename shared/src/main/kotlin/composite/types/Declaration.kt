package composite.types

import composite.Node
import composite.ResultType
import visitor.NodeResult
import visitor.NodeVisitor

class Declaration(private val left: Node, private val right: Node) : Node {
  override fun solve() : NodeResult{
    val identifier = left.solve()
    val type = right.solve()

    if (identifier.type != ResultType.IDENTIFIER) {
      throw Exception("Expected an identifier, got ${identifier.type}")
    }
    if (type.type != ResultType.DATA_TYPE) {
      throw Exception("Expected a type, got ${type.type}")
    }

    return NodeResult(ResultType.DECLARATION, identifier.primaryValue, type.primaryValue)
  }

  override fun accept(visitor: NodeVisitor){
    return
  }
}