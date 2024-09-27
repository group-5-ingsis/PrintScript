package parser.syntactic

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
    }

    private fun getAllowedStatements(version: String): List<Pair<String, StatementParser>> {
        return when (version) {
            "1.0" -> getDefaultStatements()
            "1.1" -> getDefaultStatements() + listOf(
                Pair("IF", IfStatementParser)
            )
            else -> throw Error("Version not supported")
        }
    }

    private fun getDefaultStatements(): List<Pair<String, StatementParser>> {
        return listOf(
            Pair("PRINT", PrintStatementParser),
            Pair("DECLARATION_KEYWORD", LetDeclarationParser),
            Pair("", ExpressionStatementParser)
        )
    }
}
