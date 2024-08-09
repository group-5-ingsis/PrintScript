package parser.composite

import interpreter.NodeVisitor
import parser.NodeResult
import parser.composite.types.ResultType

interface Node {
  fun solve() : NodeResult
  fun accept(visitor: NodeVisitor): NodeResult
}