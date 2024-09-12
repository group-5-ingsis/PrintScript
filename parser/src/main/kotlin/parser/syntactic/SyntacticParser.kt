package parser.syntactic

import nodes.StatementType
import parser.syntactic.statements.GenericStatementParser
import position.visitor.Environment
import position.visitor.StatementVisitor
import position.visitor.Visitor
import position.visitor.statementVisitorResult
import token.Token

object SyntacticParser {

    fun parse(tokens: List<Token>, version: String): Pair<StatementType, List<Token>> {
        val currentParser = GenericStatementParser.makeStatementParser(version)

        val (remainingTokens, exp) = currentParser.parse(tokens)
        return Pair(exp, remainingTokens)
    }

}
