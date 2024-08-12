package parser.composite.types

import parser.NodeResult
import parser.composite.Node
import visitor.NodeVisitor


class Identifier(private val value: String) : Node {

  override fun solve(): NodeResult {
    return NodeResult(ResultType.IDENTIFIER, value, null)
  }

    override fun accept(visitor: NodeVisitor): NodeResult {
        TODO("Not yet implemented")
    }

}