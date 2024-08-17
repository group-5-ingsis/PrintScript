package parser.builders

import Node
import parser.statement.Statement
import token.Token
import java.util.*

/**
 * Builder class to create a Declaration node from a given statement.
 * This represents a variable declaration without an assignment, like `let a: Number;`.
 */
class DeclarationASTBuilder : ASTBuilder {

    /**
     * Builds a Declaration node based on the content of the provided statement.
     *
     * @param statement The statement that contains tokens for a variable declaration.
     * @return A [Node.Declaration] that represents the variable declaration.
     */
    override fun build(statement: Statement): Node.Declaration {
        val tokens: List<Token> = statement.content

        // Extract the identifier and variable type from the tokens
        val identifier = Node.Identifier(tokens[1].value)
        val dataType = Node.DataType(tokens[3].value.uppercase(Locale.getDefault()))

        // Construct and return a Declaration node
        return Node.Declaration(
            dataType = dataType,
            kindVariableDeclaration = tokens[0].value,  // Extract declaration kind, e.g., "let"
            identifier = identifier.value
        )
    }
}
