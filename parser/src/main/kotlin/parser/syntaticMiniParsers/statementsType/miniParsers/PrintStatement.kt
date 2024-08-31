package parser.syntaticMiniParsers.statementsType.miniParsers

import nodes.Expression
import nodes.StatementType
import parser.syntaticMiniParsers.TokenManager
import parser.syntaticMiniParsers.expressions.miniParsers.ExpressionType
import parser.syntaticMiniParsers.statementsType.MiniStatementParser
import parser.syntaticMiniParsers.statementsType.ParseStatementResult
import token.Token

class PrintStatement: MiniStatementParser {
    override fun parse(tokens: List<Token>): ParseStatementResult {
        var manager = TokenManager(tokens)
        val position = manager.getPosition()
        val ( remainingTokens, exp) = ExpressionType.makeExpressionEvaluator().parse(manager.getTokens())
        manager = TokenManager(remainingTokens)
        manager.consumeTokenValue(";")

        return if (exp is Expression.Grouping){

            Pair(manager.getTokens(), StatementType.Print(exp, position))

        }else {

            throw Error("Expression shoud be groupping at line : " + position.line)

        }
    }
}