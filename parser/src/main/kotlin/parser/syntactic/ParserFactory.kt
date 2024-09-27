package parser.syntactic

import parser.syntactic.expressions.BinaryParser
import parser.syntactic.expressions.ExpressionParser
import parser.syntactic.expressions.PrimaryParser
import parser.syntactic.statements.DeclarationParser
import parser.syntactic.statements.StatementParser

object ParserFactory {

    fun createStatementParser(manager: TokenManager, version: String): StatementParser {
        val allowedStatements = getAllowedStatements(version)

        for ((statementType, parser) in allowedStatements) {
            val matches = manager.nextTokenIsType(statementType)

            if (matches) {
                return parser
            }
        }

        throw Error("No matching statement parser found for version $version")
    }

    fun createExpressionParser(version: String): ExpressionParser {
        val primary = PrimaryParser(version)
        return BinaryParser(primary, listOf("PLUS", "MINUS", "STAR", "SLASH", "AND", "OR"))
    }

    private fun getAllowedStatements(version: String): List<Pair<String, StatementParser>> {
        return when (version) {
            "1.0" -> getDefaultStatements("1.0")
            "1.1" -> getDefaultStatements("1.1")
            else -> throw Error("Version not supported")
        }
    }

    private fun getDefaultStatements(version: String): List<Pair<String, StatementParser>> {
        return listOf(
            Pair("DECLARATION_KEYWORD", DeclarationParser(version))
        )
    }
}
