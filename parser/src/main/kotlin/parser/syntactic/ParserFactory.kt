package parser.syntactic

import parser.syntactic.statements.StatementParser
import token.Token

object ParserFactory {

    fun createParser(tokens: List<Token>, version: String): StatementParser {
        val allowedStatements = getAllowedStatements(version)
        val manager = TokenManager(tokens)

        return object : StatementParser {
            override fun parse(tokens: List<Token>): ParseStatementResult {
                for ((statementType, typeOfStatements) in allowedStatements) {
                    val nextTokenMatchesExpectedType = manager.nextTokenMatchesExpectedType(statementType)
                    if (nextTokenMatchesExpectedType) {
                        manager.advance()
                        val tokensToParse = manager.getTokens()
                        return typeOfStatements.parse(tokensToParse)
                    }
                }

                val (_, typeOfStatements) = allowedStatements.last()
                return typeOfStatements.parse(manager.getTokens())
            }
        }
    }

    private fun getAllowedStatements(version: String): List<Pair<String, StatementParser>> {
        return when (version) {
            "1.0" -> getDefaultStatements()
            )
            "1.1" -> listOf(
                Pair("PRINT", PrintStatementParser(ExpressionType.makeExpressionEvaluatorV1_1())),
                Pair("IF", IfStatementParser(ExpressionType.makeExpressionEvaluatorV1_1(), ::createParser)),
                Pair("LEFT_BRACE", BlockStatementParser(::createParser)),
                Pair("", createParser(listOf(), "1.0")) // using the base parser here
            )
            else -> throw Error("Version not supported")
        }
    }

    private fun getDefaultStatements(): List<Pair<String, StatementParser>> {
        return listOf(
            Pair("PRINT", PrintStatementParser(ExpressionType.makeExpressionEvaluatorV1_0())),
            Pair("DECLARATION_KEYWORD", LetDeclarationParser(ExpressionType.makeExpressionEvaluatorV1_0())),
            Pair("", ExpressionStatementParser(ExpressionType.makeExpressionEvaluatorV1_0()))
        )
    }
}
