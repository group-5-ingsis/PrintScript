package composite.types

import composite.Node
import visitor.NodeResult
import visitor.NodeVisitor

class Multiplication(private val left: Node, private val right: Node): Node {
  override fun solve(): NodeResult {
    val multiplicand = left.solve().primaryValue
    val multiplier = right.solve().primaryValue

    if (multiplier !is Number || multiplicand !is Number) {
      throw IllegalArgumentException("Multiplication operation can only be performed on numbers")
    }

    return NodeResult(ResultType.LITERAL, multiplicand.toDouble() * multiplier.toDouble(), null)
  }

  override fun accept(visitor: NodeVisitor): NodeResult {
    TODO("Not yet implemented")
  }
}