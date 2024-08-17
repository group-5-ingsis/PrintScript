package parser.builders

import Node2
import parser.exceptions.UnsupportedLeafTypeException
import parser.statement.Statement
import token.Token
import java.util.*

class AssignDeclareASTBuilder : ASTBuilder {

  override fun build(statement: Statement): Node2 {


      val tokens: List<Token> = statement.content

      // Extract the declaration keyword (e.g., let, var) from the tokens
      val kindVariableDeclaration = tokens[0].value

      // Create the identifier and variable type nodes
      val identifier = Node2.Identifier(tokens[1].value)
      val variableType = Node2.DataType(tokens[3].value.uppercase(Locale.getDefault()))

      // Determine the value being assigned (literal or identifier)
      val literal = getLeafNodeType(statement)

      // Construct and return an AssignationDeclaration node
      return Node2.AssignationDeclaration(
          dataType = variableType,
          kindVariableDeclaration = kindVariableDeclaration, // Using the extracted kind
          identifier = identifier.value,
          value = literal
      )

  }
    /**
     * Returns the node type for the value being assigned in the statement.
     * The value can be a literal (e.g., number, string) or an identifier.
     *
     * @param statement The statement containing tokens for the value being assigned.
     * @return A node representing the value, which can be a [GenericLiteral] or [Identifier].
     * @throws UnsupportedLeafTypeException If the token type is not supported.
     */
    private fun getLeafNodeType(statement: Statement): Node2.AssignationValue {
        val tokens = statement.content
        val token: Token = tokens[5]
        return when (token.type) {
            "NUMBER" -> Node2.GenericLiteral(token.value, Node2.DataType("NUMBER"))
            "STRING" -> Node2.GenericLiteral(token.value, Node2.DataType("STRING"))
            "IDENTIFIER" -> Node2.Identifier(token.value)
            else -> throw UnsupportedLeafTypeException("Unexpected leaf type: ${token.type}")
        }
    }
}