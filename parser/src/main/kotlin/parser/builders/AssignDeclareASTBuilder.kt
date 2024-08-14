package parser.builders

import composite.Node
import composite.types.*
import parser.exceptions.UnsupportedLeafTypeException
import parser.statement.Statement
import token.Token

class AssignDeclareASTBuilder : ASTBuilder {

  /* ASSIGNDECLARE STRUCTURE: let a: Number = 22; // "Test"; // b; */
  override fun build(statement: Statement): Node {
    val tokens: List<Token> = statement.content
    val leafNode: Node = getLeafNodeType(tokens)
    val assignation: Node = Assignation(
      Declaration(
        Identifier(tokens[1].value),
        VariableType(tokens[3].type),
      ),
      leafNode
    )
    return assignation
  }

  private fun getLeafNodeType(tokens: List<Token>): Node {
    val token: Token = tokens[5]
    return when (token.type) {
      "NUMBER" -> NumericLiteral(token.value.toDouble())
      "STRING" -> StringLiteral(token.value)
      "IDENTIFIER" -> Identifier(token.value)
      else -> throw UnsupportedLeafTypeException("Unexpected leaf type: ${token.type}")
    }
  }
}