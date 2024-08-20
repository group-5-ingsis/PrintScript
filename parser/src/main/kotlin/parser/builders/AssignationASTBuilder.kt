package parser.builders

import parser.exceptions.UnsupportedLeafTypeException
import parser.statement.Statement
import token.Token

/**
 * Builder class to create an Assignation node from a given statement.
 * This represents a variable assignment, like `a = 4;`, `b = "test";`, or `c = b;`.
 */
class AssignationASTBuilder : ASTBuilder {
    /**
     * Builds an Assignation node based on the content of the provided statement.
     *
     * @param statement The statement that contains tokens for a variable assignment.
     * @return A [Node.Assignation] that represents the variable assignment.
     */
    override fun build(statement: Statement): Node.Assignation {
        val tokens: List<Token> = statement.content

        // Extract the identifier and the value being assigned
        val identifier = Node.Identifier(tokens[0].value)
        val assignValue: Node.AssignationValue = getLeafType(tokens[2])

        // Construct and return an Assignation node
        return Node.Assignation(
            identifier = identifier,
            value = assignValue,
        )
    }

    /**
     * Determines the type of value being assigned and returns the appropriate node.
     * The value can be a numeric literal, string literal, or identifier.
     *
     * @param token The token that represents the value being assigned.
     * @return A [Node.AssignationValue], which can be [GenericLiteral] or [Identifier] or other defined in future
     * @throws UnsupportedLeafTypeException If the token type is not supported.
     */
    private fun getLeafType(token: Token): Node.AssignationValue {
        return when (token.type) {
            "NUMBER" -> Node.GenericLiteral(token.value, Node.DataType("NUMBER"))
            "STRING" -> Node.GenericLiteral(token.value, Node.DataType("STRING"))
            "IDENTIFIER" -> Node.Identifier(token.value)
            else -> throw UnsupportedLeafTypeException(
                "Unknown assignment token type ${token.type}",
            )
        }
    }
}