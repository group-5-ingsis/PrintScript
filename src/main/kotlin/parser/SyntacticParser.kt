package parser

import Position
import parser.composite.Leaf
import parser.composite.Node
import token.Token

/* Singleton? TODO check */
class SyntacticParser {

  /* Client method for calls to the syntactic parser. */
  fun run(tokens: List<Token>): List<Node> {
    return parse(tokens)
  }

  /* Returns the reference to the root node. */
  private fun parse(tokens: List<Token>): List<Node> {
    val tokenSublists : List<List<Token>> = getTokenSublists(tokens)
    return buildAST(tokenSublists)
  }

  private fun buildAST(tokenSublists: List<List<Token>>): List<Node> {
    TODO("Not yet implemented")
  }

  private fun getTokenSublists(tokens: List<Token>): List<List<Token>> {
    val tokenSublists = mutableListOf<List<Token>>()
    var j = 0
    for ((index, token) in tokens.withIndex()) {
      if (token.type == "punctuation" && token.value == "=") {
        tokenSublists.add(tokens.subList(j, index))
        j += index + 1
      }
    }
    return tokenSublists
  }
}