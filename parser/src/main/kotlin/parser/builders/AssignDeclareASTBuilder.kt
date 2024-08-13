package parser.builders

import composite.Node
import composite.types.*
import parser.statement.Statement
import token.Token

class AssignDeclareASTBuilder : ASTBuilder {

  /* ASSIGNDECLARE STRUCTURE: let a: Number = 22; */
  override fun build(statement: Statement): Node {
    val tokens: List<Token> = statement.content
    val assignation: Node = Assignation(
      Declaration(
        Identifier(tokens[1].value),
        VariableType(tokens[3].type),
      ),
      TODO()
        //Literal()
        //tokens[5].value
    )
    return assignation
  }
}