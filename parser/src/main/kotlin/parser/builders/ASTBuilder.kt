package parser.builders

import composite.Node
import parser.statement.Statement

interface ASTBuilder {
    fun build(statement: Statement): Node
}
