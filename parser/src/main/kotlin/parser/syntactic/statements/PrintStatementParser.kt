package parser.syntactic.statements

import nodes.Expression
import nodes.Statement
import parser.syntactic.TokenManager
import token.Token

object PrintStatementParser : StatementParser {
    override fun parse(tokens: List<Token>): ParseStatementResult {
        var manager = TokenManager(tokens)
        val position = manager.getPosition()
        val (remainingTokens, exp) = expressionEvaluator.parse(manager.getTokens())
        manager = TokenManager(remainingTokens)
        manager.consumeTokenValue(";")

        return if (exp is Expression.Grouping) {
            Pair(manager.getTokens(), Statement.Print(exp, position))
        } else {
            throw Error("Expression shoud be groupping at line : " + position.line)
        }
    }
}
