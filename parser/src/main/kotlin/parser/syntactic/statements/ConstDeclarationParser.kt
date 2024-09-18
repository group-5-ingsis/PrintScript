package parser.syntactic.statements

import exception.SemanticErrorException
import nodes.Expression
import nodes.StatementType
import parser.syntactic.TokenManager
import parser.syntactic.expressions.ExpressionType
import token.Token

class ConstDeclarationParser(private val expressionEvaluator: ExpressionType) : StatementParser {

    override fun parse(tokens: List<Token>): ParseStatementResult {
        var manager = TokenManager(tokens)
        val position = manager.getPosition()

        val identifier = manager.consumeTokenType("IDENTIFIER")
        var initializer: Expression? = null

        manager.consumeTokenValue(":")
        val dataType = manager.consumeTokenType("VARIABLE_TYPE").value

        if (manager.isValue(";")) {
            throw SemanticErrorException("Invalid procedure: variable '${identifier.value}' of type 'const' cannot be declared. ")
        }

        manager.consumeTokenValue("=")
        val (remainingTokens, exp) = expressionEvaluator.parse(manager.getTokens())
        initializer = exp
        manager = TokenManager(remainingTokens)

        manager.consumeTokenValue(";")

        return Pair(manager.getTokens(), StatementType.Variable("const", identifier.value, initializer, dataType, position))
    }
}
