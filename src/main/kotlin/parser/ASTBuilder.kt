package parser

import parser.composite.Node
import parser.statement.Statement

interface ASTBuilder {
  fun build(statement: Statement) : Node
}