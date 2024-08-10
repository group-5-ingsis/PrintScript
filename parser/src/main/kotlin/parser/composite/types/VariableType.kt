package parser.composite.types

import parser.NodeResult
import parser.composite.Node

class VariableType(val value: String) : Node {

  override fun solve(): NodeResult {
    return NodeResult(ResultType.DATA_TYPE, value, null)
  }
}