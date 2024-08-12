package parser.builders

import parser.SyntacticParser
import parser.composite.Node
import parser.composite.types.Declaration
import parser.composite.types.Identifier
import parser.composite.types.VariableType
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