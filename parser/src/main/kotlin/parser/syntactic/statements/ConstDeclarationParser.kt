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
        var tokenManager = manager.consumeType("DECLARATION_KEYWORD")

        val identifierToken = tokenManager.peek()
        val identifier = identifierToken.value
        tokenManager = tokenManager.consumeType("IDENTIFIER")

        tokenManager = tokenManager.consumeValue(":")

        val dataTypeToken = tokenManager.peek()
        val dataType = dataTypeToken.value

        tokenManager = tokenManager.consumeType("VARIABLE_TYPE")

        val constDeclaration = declarationType == "CONST"
        val endOfStatement = tokenManager.isValue(";")

        if (constDeclaration && endOfStatement) {
            throw SemanticErrorException("Invalid procedure: variable '$identifier' of type 'const' cannot be declared.")
        }

        tokenManager = tokenManager.consumeValue("=")

        val expressionParser = ParserFactory.createExpressionParser(tokenManager, version)

        val initializer = expressionParser.parse(tokenManager)

        tokenManager = tokenManager.consumeValue(";")

        return Statement.Variable("const", identifier, initializer, dataType, position)
    }
}
