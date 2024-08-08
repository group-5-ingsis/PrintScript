package parser

import parser.composite.Node

interface ASTBuilder {
  fun build(statement: Statement) : Node
}