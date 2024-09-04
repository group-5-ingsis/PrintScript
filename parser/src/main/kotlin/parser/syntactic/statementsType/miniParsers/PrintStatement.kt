package parser.syntactic.statementsType.miniParsers

import nodes.Expression
import nodes.StatementType
import parser.syntactic.TokenManager
import parser.syntactic.expressions.miniParsers.ExpressionType
import parser.syntactic.statementsType.MiniStatementParser
import parser.syntactic.statementsType.ParseStatementResult
import token.Token

class PrintStatement : MiniStatementParser {
    override fun parse(tokens: List<Token>): ParseStatementResult {
        var manager = TokenManager(tokens)
        val position = manager.getPosition()
        val (remainingTokens, exp) = ExpressionType.makeExpressionEvaluator().parse(manager.getTokens())
        manager = TokenManager(remainingTokens)
        manager.consumeTokenValue(";")

        return if (exp is Expression.Grouping) {
            Pair(manager.getTokens(), StatementType.Print(exp, position))
        } else {
            throw Error("Expression shoud be groupping at line : " + position.line)
        }
    }
}
