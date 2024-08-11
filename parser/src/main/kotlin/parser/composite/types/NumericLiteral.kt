package parser.composite.types

import parser.NodeResult
import parser.composite.Node
import parser.composite.ResultType

class NumericLiteral(private val value: Number) : Node {

  override fun solve(): NodeResult {
    return NodeResult(ResultType.LITERAL, value, null)
  }

}