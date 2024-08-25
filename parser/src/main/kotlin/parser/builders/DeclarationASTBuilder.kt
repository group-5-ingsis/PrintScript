package parser.builders

import Node
import parser.statement.Statement
import token.Token
import java.util.*

class DeclarationASTBuilder : ASTBuilder {
    override fun build(statement: Statement): Node.Declaration {
        val tokens: List<Token> = statement.content

        val identifier = Node.Identifier(tokens[1].value)
        val dataType = Node.DataType(tokens[3].value.uppercase(Locale.getDefault()))

        return Node.Declaration(
            dataType = dataType,
            kindVariableDeclaration = tokens[0].value,
            identifier = identifier.value
        )
    }
}
