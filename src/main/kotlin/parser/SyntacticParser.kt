package parser

import parser.composite.Node
import token.Token

class SyntacticParser {

  /* Client method for calls to the syntactic parser. */
  fun run(tokens: List<Token>): List<Node> {
    return parse(tokens)
  }

  /* Returns the reference to the root node. */
  /* let a: string -> Declaration
  * let a : String = "a" -> AssignDeclare
  * class ASTBuilder.build(type). */
  private fun parse(tokens: List<Token>): List<Node> {
    val tokenSublists : List<List<Token>> = getTokenSublists(tokens)
    val statementList: List<Statement> = buildStatementList(tokenSublists)
    return buildAST(tokenSublists)
  }

  private fun buildStatementList(tokenSublists: List<List<Token>>): List<Statement> {
    val statementList = mutableListOf<Statement>()
    for (tokenSublist in tokenSublists) {
      statementList.add(Statement(tokenSublist))
    }
    return statementList
  }

  private fun buildAST(tokenSublists: List<List<Token>>): List<Node> {
    TODO("Not yet implemented")
  }

  private fun getTokenSublists(tokens: List<Token>): List<List<Token>> {
    val tokenSublists = mutableListOf<List<Token>>()
    var j = 0
    for ((index, token) in tokens.withIndex()) {
      if (token.type == "punctuation" && token.value == ";") {
        tokenSublists.add(tokens.subList(j, index))
        j += index + 1
      }
    }
    return tokenSublists
  }
}