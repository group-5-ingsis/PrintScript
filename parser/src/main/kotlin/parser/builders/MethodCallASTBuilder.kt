package parser.builders

import parser.statement.Statement
import token.Token

/**
 * Builder class to create a MethodCall node from a given statement.
 * This represents a method call with arguments, such as `myMethod(1, "test", varName);`.
 */
class MethodCallASTBuilder : ASTBuilder {
  /**
   * Builds a MethodCall node based on the content of the provided statement.
   *
   * @param statement The statement containing tokens for a method call.
   * @return A [Method] that represents the method call.
   */
  override fun build(statement: Statement): Node.Method {
    val tokens: List<Token> = statement.content
    val argumentsList: List<Token> = separateArguments(tokens)
    val nodes: List<Node.AssignationValue> = buildNodesForArguments(argumentsList)

    val identifier =
      Node.Identifier(
        tokens[0].value,
      )

    val arguments =
      Node.Arguments(
        nodes,
      )

    return Node.Method(
      identifier = identifier,
      arguments = arguments,
    )
  }

  /**
   * Builds nodes for each argument in the method call.
   *
   * @param arguments The tokens representing the method arguments.
   * @return A list of [AssignationValue], which can be [GenericLiteral] or [Identifier].
   */
  private fun buildNodesForArguments(arguments: List<Token>): List<Node.AssignationValue> {
    val nodes = mutableListOf<Node.AssignationValue>()
    for (token in arguments) {
      when (token.type) {
        "NUMBER" -> nodes.add(Node.GenericLiteral(token.value, Node.DataType("NUMBER")))
        "STRING" -> nodes.add(Node.GenericLiteral(token.value, Node.DataType("STRING")))
        "IDENTIFIER" -> nodes.add(Node.Identifier(token.value))
      }
    }
    return nodes
  }

  /**
   * Separates tokens representing method arguments from the entire list of tokens.
   *
   * @param tokens The tokens from the method call statement.
   * @return A list of tokens that represent method arguments.
   */
  private fun separateArguments(tokens: List<Token>): List<Token> {
    val arguments: MutableList<Token> = mutableListOf()
    for (token in tokens) {
      if (isArgument(token)) {
        arguments.add(token)
      }
    }
    return arguments
  }

  /**
   * Checks if a token is an argument (either a literal or an identifier).
   *
   * @param token The token to check.
   * @return `true` if the token is a method argument, otherwise `false`.
   */
  private fun isArgument(token: Token): Boolean {
    val isLiteral = (
      token.type == "NUMBER" ||
        token.type == "STRING" ||
        token.type == "IDENTIFIER"
    )
    val isComma = (
      token.type == "PUNCTUATION" &&
        token.value == ","
    )
    return !isComma && isLiteral
  }
}
