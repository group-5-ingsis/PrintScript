package parser.syntactic.statements

import nodes.Expression
import nodes.StatementType
import parser.syntactic.TokenManager
import parser.syntactic.expressions.ExpressionType
import token.Token

class PrintStatementParser : StatementParser {
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
