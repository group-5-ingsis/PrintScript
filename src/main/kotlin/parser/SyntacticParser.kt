package parser

import Position
import parser.composite.CompositeNode
import parser.composite.Leaf
import parser.composite.Node
import token.Token
import token.TokenType

/* Singleton? TODO check */
class SyntacticParser {

  /* Client method for calls to the syntactic parser. */
  fun run(tokens: List<Token>): Node {
    return buildAST(tokens)
  }

  /* Returns the reference to the root node. */
  private fun buildAST(tokens: List<Token>): Node {
    return Leaf(TokenType.UNKNOWN, "", Position(-1, -1))
  }
}