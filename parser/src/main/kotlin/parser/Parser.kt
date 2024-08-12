package parser

import composite.Node
import parser.exceptions.SemanticErrorException
import token.Token

/* Singleton object since it can be reused with different arguments. */
class Parser {
  private val syntacticParser = SyntacticParser()
  private val semanticParser = SemanticParser()

  /* Performs a call to both parsers. */
  @Throws(SemanticErrorException::class)
  fun run(tokens: List<Token>, parameters: List<String>): SyntacticParser.RootNode {
    val ast: SyntacticParser.RootNode = syntacticParser.run(tokens)
    return semanticParser.run(ast)
  }
}