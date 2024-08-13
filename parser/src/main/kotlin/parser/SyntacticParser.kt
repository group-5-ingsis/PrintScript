package parser

import parser.builders.ASTBuilder
import parser.builders.AssignDeclareASTBuilder
import parser.builders.AssignationASTBuilder
import composite.Node
import composite.ResultType
import parser.builders.DeclarationASTBuilder
import parser.statement.Statement
import parser.statement.StatementCategorizer
import token.Token
import visitor.NodeResult
import visitor.NodeVisitor

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
    val statements : List<Statement> = getStatements(tokens)
    val categorizedStatements = categorizer.categorize(statements)
    return buildAST(categorizedStatements)
  }

  private fun buildAST(categorizedStatements: List<Statement>): RootNode {
    val root = RootNode.create()
    for (statement in categorizedStatements) {
      val statementType = statement.statementType.toString()
      val builder = builders[statementType]
      if (builder != null) {
        val child = builder.build(statement)
        root.addChild(child)
      } else {
        throw UnsupportedOperationException("Unexpected statement")
      }
    }
    return root
  }

  private fun getStatements(tokens: List<Token>): List<Statement> {
    val statements = mutableListOf<Statement>()
    var j = 0

    for ((index, token) in tokens.withIndex()) {
      if (token.type == "PUNCTUATION" && token.value == ";") {
        val sublist = tokens.subList(j, index + 1)
        val statementType = "UNKNOWN"
        statements.add(Statement(sublist, statementType))
        j = index + 1
      }
    }

    return statements
  }


  /* Represents the root of an AST. */
  class RootNode private constructor(): Node {
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

    override fun solve(): NodeResult {
      for (child in children) {
        return child.solve()
      }

      return NodeResult(ResultType.DATA_TYPE, "", "")
    }

    override fun accept(visitor: NodeVisitor) {
      for (child in children) {
        child.accept(visitor)
      }
    }

  }
}