package parser.syntactic

import nodes.Statement
import parser.syntactic.statements.GenericStatementParser
import token.Token

object SyntacticParser {

    fun parse(tokens: List<Token>, version: String): Pair<Statement, List<Token>> {
        val currentParser = GenericStatementParser.makeStatementParser(version)

        val (remainingTokens, exp) = currentParser.parse(tokens)
        return Pair(exp, remainingTokens)
    }
}
