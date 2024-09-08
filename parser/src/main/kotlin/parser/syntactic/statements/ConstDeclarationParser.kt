package parser.syntactic.statements

import nodes.StatementType
import parser.syntactic.TokenManager
import parser.syntactic.expressions.ExpressionType
import token.Token

class ConstDeclarationParser : StatementParser {
    override fun parse(tokens: List<Token>): ParseStatementResult {
        val manager = TokenManager(tokens)

        val position = manager.getPosition()
        val identifier = manager.consumeTokenType("IDENTIFIER")
        manager.consumeTokenValue(":")
        val dataType = manager.consumeTokenType("VARIABLE_TYPE").value
        manager.consumeTokenValue("=")
        val initializer = ExpressionType.makeExpressionEvaluator().parse(manager.getTokens())

        return Pair(initializer.first, StatementType.Variable("const", identifier.value, initializer.second, dataType, position))
    }
}
