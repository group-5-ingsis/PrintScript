package parser.builders

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import parser.SyntacticParser
import parser.composite.Node
import parser.composite.types.*
import parser.exceptions.UnsupportedLeafTypeException
import parser.statement.Statement
import token.Token

class AssignationASTBuilder : ASTBuilder {

  /* ASSIGNATION STRUCTURE: a = 4; // b = "test"; // c = b;  */
  override fun build(statement: Statement): Node {
    val tokens: List<Token> = statement.content
    val leafType: Node = getLeafType(tokens[2])
    return Assignation(
      Identifier(tokens[0].value),
      leafType
    )
  }

  private fun getLeafType(token: Token): Node {
    return when (token.type) {
      "NUMBER" -> NumericLiteral(token.value.toFloat())
      "STRING" -> StringLiteral(token.value)
      "IDENTIFIER" -> Identifier(token.value)
      else -> throw UnsupportedLeafTypeException("Unknown assignment token type ${token.type}")
    }
  }
}