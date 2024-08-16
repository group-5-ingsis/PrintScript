package parser.builders


import Node2
import parser.statement.Statement

interface ASTBuilder {
  fun build(statement: Statement) : Node2
}