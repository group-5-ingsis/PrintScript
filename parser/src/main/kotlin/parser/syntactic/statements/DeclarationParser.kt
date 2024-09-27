package parser.syntactic.statements

import exception.SemanticErrorException
import nodes.Statement
import parser.syntactic.ParserFactory
import parser.syntactic.TokenManager

class DeclarationParser(private val version: String) : StatementParser {

    override fun parse(manager: TokenManager): Statement {
        val declarationType = parseDeclarationType(manager)
        var tokenManager = manager.consumeType("DECLARATION_KEYWORD")

        val identifier = parseIdentifier(tokenManager)
        tokenManager = tokenManager.consumeType("IDENTIFIER")

        tokenManager = tokenManager.consumeValue(":")

        val dataType = parseDataType(tokenManager)
        tokenManager = tokenManager.consumeType("VARIABLE_TYPE")

        validateConstDeclaration(declarationType, identifier, tokenManager)

        tokenManager = tokenManager.consumeValue("=")

        val initializer = parseInitializer(tokenManager)

        tokenManager = tokenManager.consumeValue(";")

        val position = manager.getPosition()
        return Statement.Variable(declarationType, identifier, initializer, dataType, position)
    }

    private fun parseDeclarationType(manager: TokenManager): String {
        return manager.peek().value
    }

    private fun parseIdentifier(manager: TokenManager): String {
        val identifierToken = manager.peek()
        return identifierToken.value
    }

    private fun parseDataType(manager: TokenManager): String {
        val dataTypeToken = manager.peek()
        return dataTypeToken.value
    }

    private fun validateConstDeclaration(declarationType: String, identifier: String, manager: TokenManager) {
        if (declarationType == "CONST" && manager.isValue(";")) {
            throw SemanticErrorException("Invalid procedure: variable '$identifier' of type 'const' cannot be declared.")
        }
    }

    private fun parseInitializer(manager: TokenManager): nodes.Expression {
        val expressionParser = ParserFactory.createExpressionParser(manager, version)
        return expressionParser.parse(manager)
    }
}
