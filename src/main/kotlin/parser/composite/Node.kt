package parser.composite

import parser.NodeResult
import parser.composite.types.ResultType

interface Node {
  fun solve() : NodeResult
}