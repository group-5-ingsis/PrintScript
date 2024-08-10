package parser.composite

import parser.NodeResult

interface Node {
  fun solve() : NodeResult
}