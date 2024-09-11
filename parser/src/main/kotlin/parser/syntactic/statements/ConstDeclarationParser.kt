package parser.syntactic.statements

import exception.SemanticErrorException
import nodes.StatementType
import parser.syntactic.TokenManager
import parser.syntactic.expressions.ExpressionType
import token.Token

class ConstDeclarationParser(private val expressionEvaluator: ExpressionType) : StatementParser {

    override fun parse(tokens: List<Token>): ParseStatementResult {
        val manager = TokenManager(tokens)
        val position = manager.getPosition()
        val identifier = manager.consumeTokenType("IDENTIFIER")
        manager.consumeTokenValue(":")
        val dataType = manager.consumeTokenType("VARIABLE_TYPE").value
        if (manager.isValue(";")) {
            throw SemanticErrorException("Invalid procedure: variable '${identifier.value}' of type 'const' cannot be declared. ")
        }
        manager.consumeTokenValue("=")
        val initializer = expressionEvaluator.parse(manager.getTokens())
        return Pair(initializer.first, StatementType.Variable("const", identifier.value, initializer.second, dataType, position))
    }
}
