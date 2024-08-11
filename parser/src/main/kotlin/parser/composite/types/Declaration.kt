package parser.composite.types

import parser.NodeResult
import parser.composite.Node
import parser.composite.ResultType

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

    println("Declared variable '${identifier.primaryValue}' as type ${type.primaryValue}")
    return NodeResult(ResultType.DECLARATION, identifier.primaryValue, type.primaryValue)
  }
}