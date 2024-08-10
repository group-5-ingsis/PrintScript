package parser.composite

import interpreter.NodeVisitor
import parser.NodeResult

interface Node {
  fun solve() : NodeResult
  fun accept(visitor: NodeVisitor): NodeResult
}