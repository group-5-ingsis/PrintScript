package parser.syntactic.statements

import exception.SemanticErrorException
import nodes.Statement
import parser.syntactic.ParserFactory
import parser.syntactic.TokenManager

class ConstDeclarationParser(private val version: String) : StatementParser {

    override fun parse(manager: TokenManager): Statement {
        val position = manager.getPosition()

        val declarationToken = manager.peek()
        val declarationType = declarationToken.value
        var tokenManager = manager.consume("DECLARATION_KEYWORD")

        val identifierToken = tokenManager.peek()
        val identifier = identifierToken.value
        tokenManager = tokenManager.consume("IDENTIFIER")

        tokenManager = tokenManager.consume(":")

        val dataType = manager.peek().value

        tokenManager = tokenManager.consume("VARIABLE_TYPE")

        if (tokenManager.isValue(";") && declarationType == "CONST") {
            throw SemanticErrorException("Invalid procedure: variable '$identifier' of type 'const' cannot be declared.")
        }

        tokenManager = tokenManager.consume("=")

        val expressionParser = ParserFactory.createExpressionParser(tokenManager, version)

        val initializer = expressionParser.parse(tokenManager)

        tokenManager = tokenManager.consume(";")

        return Statement.Variable("const", identifier, initializer, dataType, position)
    }
}
