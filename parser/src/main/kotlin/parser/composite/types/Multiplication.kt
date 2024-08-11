package parser.composite.types

import parser.NodeResult
import parser.composite.Node
import parser.composite.ResultType

class Multiplication(private val left: Node, private val right: Node): Node {
  override fun solve(): NodeResult {
    val multiplicand = left.solve().primaryValue
    val multiplier = right.solve().primaryValue

    if (multiplier !is Number || multiplicand !is Number) {
      throw IllegalArgumentException("Multiplication operation can only be performed on numbers")
    }

    return NodeResult(ResultType.LITERAL, multiplicand.toDouble() * multiplier.toDouble(), null)
  }
}