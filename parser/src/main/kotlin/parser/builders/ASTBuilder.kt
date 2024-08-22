package parser.builders

import Node
import parser.statement.Statement

interface ASTBuilder {
  fun build(statement: Statement): Node
}
