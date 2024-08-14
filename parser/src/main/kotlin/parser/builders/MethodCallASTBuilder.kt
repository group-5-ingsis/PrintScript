package parser.builders

import composite.Node
import composite.types.*
import parser.statement.Statement
import token.Token

class MethodCallASTBuilder : ASTBuilder {

  /* println(32) // sum(1, 53, 52, ...) */
  /* IDENTIFIER, PUNCTUATION, ARG, ..., PUNCTUATION, PUNCTUATION */
  override fun build(statement: Statement): Node {
    val tokens: List<Token> = statement.content
    val argumentsList: List<Token> = separateArguments(tokens)
    val nodes: List<Node> = buildNodesForArguments(argumentsList)

    val identifier = Identifier(
      tokens[0].value
    )

    val arguments = Arguments(
      nodes
    )

    return MethodCall(
      identifier,
      arguments
    )
  }

  private fun buildNodesForArguments(arguments: List<Token>): List<Node> {
    val nodes = mutableListOf<Node>()
    for (token in arguments) {
      when (token.type) {
        "NUMBER" -> nodes.add(NumericLiteral(token.value.toDouble()))
        "STRING" -> nodes.add(StringLiteral(token.value))
        "IDENTIFIER" -> nodes.add(Identifier(token.value))
      }
    }
    return nodes
  }

  private fun separateArguments(tokens: List<Token>): List<Token> {
    val arguments: MutableList<Token> = mutableListOf()
    for (token in tokens) {
      if (isArgument(token)) {
        arguments.add(token)
      }
    }
    return arguments
  }

  private fun isArgument(token: Token): Boolean {
    val isLiteral = (
            token.type == "NUMBER"
            || token.type == "STRING"
            || token.type == "IDENTIFIER")
    val isComma = (
            token.type == "PUNCTUATION"
            && token.value == ","
            )
    return !isComma && isLiteral
  }
}