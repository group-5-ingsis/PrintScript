package parser.builders

import parser.SyntacticParser
import parser.composite.Node
import parser.composite.types.Assignation
import parser.composite.types.Identifier
import parser.composite.types.Literal
import parser.composite.types.VariableType
import parser.statement.Statement
import token.Token

class AssignationASTBuilder : ASTBuilder {

  /* ASSIGNATION STRUCTURE: a = 23 \\ b = "test" \\ c = b; */
  override fun build(statement: Statement, root: SyntacticParser.RootNode): Node {
    val tokens: List<Token> = statement.content
    //val isLiteral = tokens[0].value is Literal
    return Assignation(
      Identifier(tokens[1].value),
      VariableType(tokens[3].type)
    )
    TODO("Not yet implemented")
  }
}