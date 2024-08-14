package parser.builders

import composite.Node
import composite.types.*
import parser.exceptions.UnsupportedLeafTypeException
import parser.statement.Statement
import token.Token

class AssignDeclareASTBuilder : ASTBuilder {

  override fun build(statement: Statement): Node {

    val tokens: List<Token> = statement.content


    val identifier = Identifier(tokens[1].value)
    val variableType = VariableType(tokens[3].type)
    val literal = getLeafNodeType(statement)

    val assignationDeclaration: Node = AssignationDeclaration(
      Declaration(identifier, variableType),
      Assignation(identifier, literal)
    )

    return assignationDeclaration

  }

  private fun getLeafNodeType(statement: Statement): Node {
    val tokens = statement.content
    val token: Token = tokens[5]
    return when (token.type) {
      "NUMBER" -> NumericLiteral(token.value.toDouble())
      "STRING" -> StringLiteral(token.value)
      "IDENTIFIER" -> Identifier(token.value)
      else -> throw UnsupportedLeafTypeException("Unexpected leaf type: ${token.type}")
    }
  }
}