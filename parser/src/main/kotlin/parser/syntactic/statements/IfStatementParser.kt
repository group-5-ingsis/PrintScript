package parser.syntactic.statements

import nodes.StatementType
import org.w3c.dom.xpath.XPathEvaluator
import parser.syntactic.TokenManager
import parser.syntactic.expressions.ExpressionType
import token.Token

class IfStatementParser(private val expresionEvaluatorV_1_1: ExpressionType, val statementEvaluator: ()-> StatementParser) : StatementParser {

    override fun parse(tokens: List<Token>): ParseStatementResult {
        val stmEvaluator = statementEvaluator()

        val manager = TokenManager(tokens)

        manager.consumeTokenValue("(")
        val (remainingTokens, condition) = expresionEvaluatorV_1_1.parse(manager.getTokens())
        val newManager = TokenManager(remainingTokens)
        newManager.consumeTokenValue(")")
        val currentPosition = newManager.getPosition()
        val (remainingTokens2, thenBranch) = stmEvaluator.parse(newManager.getTokens())
        if (thenBranch !is StatementType.BlockStatement) {
            throw Error("Then branch should be a block statement in : $currentPosition")
        }
        val newManager2 = TokenManager(remainingTokens2)
        val (elseBranch, resTokens3) = if (newManager2.nextTokenMatchesExpectedType("ELSE")) {
            newManager2.advance()
            val (remainingTokens3, elseBranchReturn) = stmEvaluator.parse(newManager2.getTokens())
            val currentPosition2 = newManager2.getPosition()
            if (elseBranchReturn !is StatementType.BlockStatement) {
                throw Error("Else branch should be a block statement in : $currentPosition2")
            }
            Pair(elseBranchReturn, remainingTokens3)
        } else {
            Pair(null, newManager2.getTokens())
        }
        return Pair(resTokens3, StatementType.ifStatement(currentPosition, condition, thenBranch, elseBranch))

    }
}