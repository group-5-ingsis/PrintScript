package parser.composite.types

import parser.NodeResult
import parser.composite.Node

class Identifier(val value: String) : Node {

  override fun solve(): NodeResult {
    return NodeResult(ResultType.IDENTIFIER, value, null)
  }

}