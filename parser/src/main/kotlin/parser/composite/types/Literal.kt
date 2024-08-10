package parser.composite.types

import arrow.core.Either
import parser.NodeResult
import parser.composite.Node

class Literal(private val value : Either<String, Number>) : Node {
  override fun solve(): NodeResult {
    return NodeResult(ResultType.LITERAL, value, null)
  }
}