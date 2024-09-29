package parser.syntactic

import exception.InvalidSyntaxException
import parser.syntactic.expressions.AssignmentParser
import parser.syntactic.expressions.ExpressionParser
import parser.syntactic.expressions.GroupingParser
import parser.syntactic.expressions.LiteralParser
import parser.syntactic.expressions.VariableParser
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

    // Separar por verison (asi agarra boolean como tipo)
    fun createExpressionParser(manager: TokenManager, version: String): ExpressionParser {
        return when {
            manager.nextTokenIsType("ASSIGNMENT") -> AssignmentParser(version)
            manager.nextTokenIsType("IDENTIFIER") -> VariableParser()
            manager.nextTokenIsType("NUMBER") -> LiteralParser()
            manager.nextTokenIsType("STRING") -> LiteralParser()
            manager.nextTokenIsType("(") -> GroupingParser(version)
            else -> {
                val token = manager.peek()
                throw InvalidSyntaxException("Unexpected token: $token")
            }
        }
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
