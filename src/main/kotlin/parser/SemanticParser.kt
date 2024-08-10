package parser

import parser.composite.Node
import parser.exceptions.SemanticErrorException
import kotlin.jvm.Throws

class SemanticParser {

  @Throws(SemanticErrorException::class)
  fun run(ast: SyntacticParser.RootNode): SyntacticParser.RootNode {
    val result: SemanticParsingResult = validateAST(ast)
    if (result.isInvalid) {
      throw SemanticErrorException("Illegal procedure: " + result.message)
    } else {
      return ast
    }
  }

  /* TODO replace List<Node> for SemanticParsingResult object.  */
  @Throws(SemanticErrorException::class)
  private fun validateAST(ast: SyntacticParser.RootNode): SemanticParsingResult {
    /* Logic for validating semantically */
    return SemanticParsingResult(false, null, "")
  }
}