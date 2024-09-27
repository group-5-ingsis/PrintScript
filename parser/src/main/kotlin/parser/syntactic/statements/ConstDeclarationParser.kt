package parser.syntactic.statements

import exception.SemanticErrorException
import nodes.Statement
import parser.syntactic.ParserFactory
import parser.syntactic.TokenManager

class ConstDeclarationParser(private val version: String) : StatementParser {

    override fun parse(manager: TokenManager): Statement {
        val position = manager.getPosition()

        val identifier = manager.consume("IDENTIFIER").peek().value

        var updatedManager = manager.consume(":")

        val dataType = manager.peek().value

        updatedManager = updatedManager.consume("VARIABLE_TYPE")

        if (updatedManager.isValue(";")) {
            throw SemanticErrorException("Invalid procedure: variable '$identifier' of type 'const' cannot be declared.")
        }

        updatedManager = updatedManager.consume("=")

        val expressionParser = ParserFactory.createExpressionParser(updatedManager, version)

        val initializer = expressionParser.parse(updatedManager)

        updatedManager = updatedManager.consume(";")

        return Statement.Variable("const", identifier, initializer, dataType, position)
    }
}
