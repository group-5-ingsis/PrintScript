package parser

import Position
import parser.composite.Leaf
import parser.composite.Node
import token.Token

/* Singleton? TODO check */
class SyntacticParser {

  /* Client method for calls to the syntactic parser. */
  fun run(tokens: List<Token>): Node {
    return buildAST(tokens)
  }

  /* Returns the reference to the root node. */
  private fun buildAST(tokens: List<Token>): Node {
    val tokenSublists : List<List<Token>> = getTokenSublists(tokens)
    TODO()
  }

  private fun getTokenSublists(tokens: List<Token>): List<List<Token>> {
    val tokenSublists = mutableListOf<List<Token>>()
    for ((index, token) in tokens.withIndex()) {

    }
    TODO()
  }
}