package parser.syntactic

import parser.syntactic.expressions.AssignmentParser
import parser.syntactic.expressions.ExpressionParser
import parser.syntactic.statements.ConstDeclarationParser
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

    fun createExpressionParser(manager: TokenManager, version: String): ExpressionParser {
        val allowedExpressions = getAllowedExpressions(version)

        for ((expression, parser) in allowedExpressions) {
            val matches = manager.nextTokenIsType(expression)

            if (matches) {
                return parser
            }
        }

        throw Error("No matching statement parser found for version $version")
    }

    private fun getAllowedStatements(version: String): List<Pair<String, StatementParser>> {
        return when (version) {
            "1.0" -> getDefaultStatements("1.0")
            "1.1" -> getDefaultStatements("1.1") + listOf()
            else -> throw Error("Version not supported")
        }
    }

    private fun getDefaultStatements(version: String): List<Pair<String, StatementParser>> {
        return listOf(
            Pair("DECLARATION_KEYWORD", ConstDeclarationParser(version))
        )
    }

    private fun getAllowedExpressions(version: String): List<Pair<String, ExpressionParser>> {
        return when (version) {
            "1.0" -> getDefaultExpressions("1.0")
            else -> throw Error("Version not supported")
        }
    }

    private fun getDefaultExpressions(version: String): List<Pair<String, ExpressionParser>> {
        return listOf(
            Pair("ASSIGN", AssignmentParser(version))
        )
    }
}
