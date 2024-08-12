package parser

import parser.builders.ASTBuilder
import parser.builders.AssignDeclareASTBuilder
import parser.builders.AssignationASTBuilder
import parser.builders.DeclarationASTBuilder
import parser.composite.Node
import parser.statement.Statement
import parser.statement.StatementCategorizer
import parser.statement.UnknownStatement
import token.Token

class SyntacticParser {
  private val categorizer: StatementCategorizer = StatementCategorizer()
  /* Command pattern */
  private val builders: Map<String, ASTBuilder> = mapOf(
    "Declaration" to DeclarationASTBuilder(),
    "Assignation" to AssignationASTBuilder(),
    "AssignDeclare" to AssignDeclareASTBuilder(),

  )

  /* Client method for calls to the syntactic parser. */
  fun run(tokens: List<Token>): RootNode {
    return parse(tokens)
  }

  private fun parse(tokens: List<Token>): RootNode {
    val tokenSublist : List<List<Token>> = getTokenSublists(tokens)
    val statementList: List<Statement> = buildStatementList(tokenSublist)
    val categorizedStatements = categorizer.categorize(statementList)
    return buildAST(categorizedStatements)
  }

  private fun buildAST(categorizedStatements: List<Statement>): RootNode {
    val root = RootNode.create()
    for (statement in categorizedStatements) {
      val builder = builders[statement.statementType.toString()]
      if (builder != null) {
        root.addChild(builder.build(statement))
      } else {
        throw UnsupportedOperationException("Unexpected statement")
      }
    }
    return root
  }

  private fun buildStatementList(tokenSublists: List<List<Token>>): List<Statement> {

    val statementList = mutableListOf<Statement>()
    for (tokenSublist in tokenSublists) {

      val statement = Statement(tokenSublist, UnknownStatement())
      statementList.add(statement)

    }
    return statementList
  }

  /* IDEA: Take additional parameters to indicate according to which symbol to split. */
  private fun getTokenSublists(tokens: List<Token>): List<List<Token>> {
    val tokenSublists = mutableListOf<List<Token>>()
    var j = 0
    for ((index, token) in tokens.withIndex()) {
      if (token.type == "PUNCTUATION" && token.value == ";") {
        tokenSublists.add(tokens.subList(j, index))
        j += index + 1
      }
    }
    return tokenSublists
  }

  /* Represents the root of an AST. */
  class RootNode private constructor() {
    private val children = mutableListOf<Node>() // Each Node is a reference to a subtree. Each subtree is a Statement.

    fun addChild(child: Node) {
      children.add(child)
    }

    fun removeChild(child: Node) {
      children.remove(child)
    }

    fun getChildren(): List<Node> {
      return children
    }

    companion object {
      internal fun create(): RootNode {
        return RootNode()
      }
    }
  }
}