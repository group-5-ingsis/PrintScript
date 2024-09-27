package parser.syntactic

import parser.syntactic.expressions.ExpressionType
import parser.syntactic.statements.ExpressionStatementParser
import parser.syntactic.statements.IfStatementParser
import parser.syntactic.statements.LetDeclarationParser
import parser.syntactic.statements.PrintStatementParser
import parser.syntactic.statements.StatementParser
import token.Token

object ParserFactory {

    fun createStatementParser(tokens: List<Token>, version: String): StatementParser {
        val allowedStatements = getAllowedStatements(version)
        val manager = TokenManager(tokens)

        for ((statementType, statementParser) in allowedStatements) {
            val nextTokenMatchesExpectedType = manager.nextTokenMatchesExpectedType(statementType)
            if (nextTokenMatchesExpectedType) {
                manager.advance()
                val tokensToParse = manager.getTokens()
                return statementParser.parse(tokensToParse)
            }
        }

        val (_, statementParser) = allowedStatements.last()
        val remainingTokens = manager.getTokens()
        return statementParser.parse(remainingTokens)
    }

    private fun getAllowedStatements(version: String): List<Pair<String, StatementParser>> {
        return when (version) {
            "1.0" -> getDefaultStatements()
            "1.1" -> getDefaultStatements() + listOf(
                Pair("IF", IfStatementParser(ExpressionType.Companion.makeExpressionEvaluatorV1_1()))
            )
            else -> throw Error("Version not supported")
        }
    }

    private fun getDefaultStatements(): List<Pair<String, StatementParser>> {
        return listOf(
            Pair("PRINT", PrintStatementParser(ExpressionType.Companion.makeExpressionEvaluatorV1_0())),
            Pair("DECLARATION_KEYWORD", LetDeclarationParser(ExpressionType.Companion.makeExpressionEvaluatorV1_0())),
            Pair("", ExpressionStatementParser(ExpressionType.Companion.makeExpressionEvaluatorV1_0()))
        )
    }
}
