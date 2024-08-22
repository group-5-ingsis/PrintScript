package parser.builders

import Node
import parser.exceptions.UnsupportedLeafTypeException
import parser.statement.Statement
import token.Token
import java.util.*

class AssignDeclareASTBuilder : ASTBuilder {
  override fun build(statement: Statement): Node {
    val tokens: List<Token> = statement.content

    val kindVariableDeclaration = tokens[0].value

    val identifier = Node.Identifier(tokens[1].value)
    val variableType = Node.DataType(tokens[3].value.uppercase(Locale.getDefault()))

    val literal = getLeafNodeType(statement)

    return Node.AssignationDeclaration(
      dataType = variableType,
      kindVariableDeclaration = kindVariableDeclaration,
      identifier = identifier.value,
      value = literal,
    )
  }

  private fun getLeafNodeType(statement: Statement): Node.AssignableValue {
    val tokens = statement.content
    val token: Token = tokens[5]
    val isOperation = tokens[6].type == "OPERATOR"
    if (isOperation) {
      return makeBinaryOperator(tokens)
    }
    return getTokenNode(token)
  }

  private fun getTokenNode(token: Token): Node.AssignableValue {
    return when (token.type) {
      "NUMBER" -> Node.GenericLiteral(token.value, Node.DataType("NUMBER"))
      "STRING" -> Node.GenericLiteral(token.value, Node.DataType("STRING"))
      "IDENTIFIER" -> Node.Identifier(token.value)
      else -> throw UnsupportedLeafTypeException("Unexpected leaf type: ${token.type}")
    }
  }

  private fun makeBinaryOperator(tokens: List<Token>): Node.AssignableValue {
    return Node.BinaryOperations(
      symbol = tokens[6].value,
      left = getTokenNode(tokens[5]),
      right = getTokenNode(tokens[7]),
    )
  }
}
