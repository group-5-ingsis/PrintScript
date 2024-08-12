package parser.builders

import parser.SyntacticParser
import composite.Node
import parser.statement.Statement

interface ASTBuilder {
  fun build(statement: Statement) : Node
}