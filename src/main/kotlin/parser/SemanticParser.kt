package parser

import parser.composite.Node
import kotlin.jvm.Throws

class SemanticParser {

  @Throws(SemanticErrorException::class)
  fun run(ast: List<Node>): List<Node> {
    return validateAST(ast)
  }

  @Throws(SemanticErrorException::class)
  private fun validateAST(ast: List<Node>): List<Node> {
    /* Logic for validating semantically */
    return ast
  }
}