package composite.types

import composite.Node
import composite.ResultType
import visitor.NodeResult
import visitor.NodeVisitor

class Declaration(private val left: Node, private val right: Node) : Node {
  override fun solve() : NodeResult{

    val identifier = left.solve()
    val type = right.solve()

    checkIfValidTypes(identifier, type)

    return NodeResult(ResultType.DECLARATION, identifier.primaryValue, type.primaryValue)
  }

  private fun checkIfValidTypes(identifier: NodeResult, type: NodeResult) {
    if (identifier.type != ResultType.IDENTIFIER) {
      throw Exception("Expected an identifier, got ${identifier.type}")
      }
    if (type.type != ResultType.DATA_TYPE) {
      throw Exception("Expected a type, got ${type.type}")
      }
  }

  override fun accept(visitor: NodeVisitor){
    return visitor.visitDeclaration(this)
  }
}