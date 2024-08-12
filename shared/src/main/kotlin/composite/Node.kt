package composite

import visitor.NodeResult
import visitor.NodeVisitor

interface Node {
  fun solve() : NodeResult
  fun accept(visitor: NodeVisitor): NodeResult
}