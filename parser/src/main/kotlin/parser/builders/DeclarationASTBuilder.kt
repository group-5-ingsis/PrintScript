package parser.builders

import parser.SyntacticParser
import composite.Node
import composite.types.Declaration
import composite.types.Identifier
import composite.types.VariableType
import parser.statement.Statement
import token.Token

class DeclarationASTBuilder : ASTBuilder {

  /* DECLARATION STRUCTURE: let a: Number; */
  override fun build(statement: Statement): Node {
    val tokens: List<Token> = statement.content
    return Declaration(
      Identifier(tokens[1].value),
      VariableType(tokens[3].value)
    )
  }
}