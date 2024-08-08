package parser.composite.types

import parser.NodeResult
import parser.composite.Node

class Subtraction(private val left : Node, private val right : Node) : Node{
  override fun solve(): NodeResult {
    val minuend = left.solve()
    val subtrahend = right.solve()

    if (minuend.primaryValue !is Number || subtrahend.primaryValue !is Number) {
      throw Exception("Expected numbers, got ${minuend.primaryValue} and ${subtrahend.primaryValue}")
    }

    return NodeResult(ResultType.LITERAL, (minuend.primaryValue).toDouble() - (subtrahend.primaryValue).toDouble(), null)
  }
}