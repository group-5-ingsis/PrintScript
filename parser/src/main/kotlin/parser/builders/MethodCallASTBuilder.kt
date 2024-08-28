package parser.builders

import composite.Node
import parser.statement.Statement
import token.Token

class MethodCallASTBuilder : ASTBuilder {
    override fun build(statement: Statement): Node.Method {
        val tokens: List<Token> = statement.content
        val argumentsList: List<Token> = separateArguments(tokens)
        val nodes: List<Node.AssignableValue> = buildNodesForArguments(argumentsList)

        val identifier =
            Node.Identifier(
                tokens[0].value
            )

        val arguments =
            Node.Arguments(
                nodes
            )

        return Node.Method(
            identifier = identifier,
            arguments = arguments,
            identifierPosition = tokens[0].position
        )
    }

    private fun buildNodesForArguments(arguments: List<Token>): List<Node.AssignableValue> {
        val nodes = mutableListOf<Node.AssignableValue>()
        for (token in arguments) {
            when (token.type) {
                "NUMBER" -> nodes.add(Node.GenericLiteral(token.value, Node.DataType("NUMBER")))
                "STRING" -> nodes.add(Node.GenericLiteral(token.value, Node.DataType("STRING")))
                "IDENTIFIER" -> nodes.add(Node.Identifier(token.value))
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
