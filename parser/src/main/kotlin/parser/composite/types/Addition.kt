package parser.composite.types

import parser.NodeResult
import parser.composite.Node
import parser.composite.ResultType

class Addition(private val left : Node, private val right : Node) : Node {
  override fun solve(): NodeResult {
    val addendA = left.solve()
    val addendB = right.solve()
    if (addendA.type != ResultType.LITERAL || addendB.type != ResultType.LITERAL) {
      throw Exception("Expected strings or numbers, got ${addendA.type} and ${addendB.type}")
    }

    return if (addendA.primaryValue is String && addendB.primaryValue is String) {
      NodeResult(ResultType.LITERAL, addendA.primaryValue + addendB.primaryValue, null)
    } else if (addendA.primaryValue is Number && addendB.primaryValue is Number) {
      NodeResult(ResultType.LITERAL, (addendA.primaryValue).toDouble() + (addendB.primaryValue).toDouble(), null)
    } else if (addendA.primaryValue is String && addendB.primaryValue is Number) {
      NodeResult(ResultType.LITERAL, addendA.primaryValue + (addendB.primaryValue).toString(), null)
    } else if (addendA.primaryValue is Number && addendB.primaryValue is String) {
      NodeResult(ResultType.LITERAL, (addendA.primaryValue).toString() + addendB.primaryValue, null)
    } else {
      throw Exception("Expected strings or numbers, got ${addendA.primaryValue} and ${addendB.primaryValue}")
    }
  }
}