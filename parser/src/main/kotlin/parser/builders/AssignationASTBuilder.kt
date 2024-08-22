package parser.builders

import parser.exceptions.UnsupportedLeafTypeException
import parser.statement.Statement
import token.Token

class AssignationASTBuilder : ASTBuilder {
  override fun build(statement: Statement): Node.Assignation {
    val tokens: List<Token> = statement.content

    val identifier = Node.Identifier(tokens[0].value)
    val assignValue: Node.AssignableValue = getLeafType(tokens[2])

    return Node.Assignation(
      identifier = identifier,
      value = assignValue,
    )
  }

  private fun getLeafType(token: Token): Node.AssignableValue {
    return when (token.type) {
      "NUMBER" -> Node.GenericLiteral(token.value, Node.DataType("NUMBER"))
      "STRING" -> Node.GenericLiteral(token.value, Node.DataType("STRING"))
      "IDENTIFIER" -> Node.Identifier(token.value)
      else -> throw UnsupportedLeafTypeException(
        "Unknown assignment token type ${token.type}",
      )
    }
  }
}
