package parser

import parser.composite.Node
import token.Token

class SyntacticParser {
  private val categorizer: StatementCategorizer = StatementCategorizer()

  /* Client method for calls to the syntactic parser. */
  fun run(tokens: List<Token>): List<Node> {
    return parse(tokens)
  }

  /* Returns the reference to the root node. */
  private fun parse(tokens: List<Token>): List<Node> {
    val tokenSublists : List<List<Token>> = getTokenSublists(tokens)
    val statementList: List<Statement> = buildStatementList(tokenSublists)
    //return buildAST(categorizer.categorize(statementList))
    TODO()
  }

  private fun buildStatementList(tokenSublists: List<List<Token>>): List<Statement> {
    val statementList = mutableListOf<Statement>()
    for (tokenSublist in tokenSublists) {
      statementList.add(Statement(tokenSublist))
    }
    return statementList
  }

  private fun buildAST(statements: List<Statement>): List<Node> {
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