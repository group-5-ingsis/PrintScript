package parser.syntaticMiniParsers.statementsType.miniParsers

import nodes.DataTypeManager
import nodes.Expression
import nodes.StatementType
import parser.syntaticMiniParsers.TokenManager
import parser.syntaticMiniParsers.expressions.miniParsers.ExpressionType
import parser.syntaticMiniParsers.statementsType.MiniStatementParser
import parser.syntaticMiniParsers.statementsType.ParseStatementResult
import token.Token

class LetDeclaration: MiniStatementParser {
    override fun parse(tokens: List<Token>): ParseStatementResult {
        var manager = TokenManager(tokens)
        val position = manager.getPosition()
        // a : Number =
        var initializer : Expression? = null
        val identifier = manager.consumeTokenType("IDENTIFIER")
        manager.consumeTokenValue(":")
        val dataType = manager.consumeTokenType("VARIABLE_TYPE").value

        if (manager.isValue("=")) {

            manager.consumeTokenValue("=")
            val (remainingTokens, exp) = ExpressionType.makeExpressionEvaluator().parse(manager.getTokens())
            initializer = exp
            manager = TokenManager(remainingTokens)
            DataTypeManager.checkDataTypeIsOkWithExpression(initializer, dataType)
        }

        manager.consumeTokenValue(";")


        return Pair(manager.getTokens(), StatementType.Variable("let", identifier.value, initializer, dataType, position))

    }
}