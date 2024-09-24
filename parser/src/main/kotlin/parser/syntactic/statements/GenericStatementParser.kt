package parser.syntactic.statements

import parser.syntactic.TokenManager
import token.Token

class GenericStatementParser(private val nextStatementsList: List<Pair<String, StatementParser>>) : StatementParser {

    override fun parse(tokens: List<Token>): ParseStatementResult {
        val manager = TokenManager(tokens)

        for ((statementType, typeOfStatements) in nextStatementsList) {
            val nextTokenMatchesExpectedType = manager.nextTokenMatchesExpectedType(statementType)
            if (nextTokenMatchesExpectedType) {
                manager.advance()
                val tokens = manager.getTokens()
                return typeOfStatements.parse(tokens)
            }
        }

        val (_, typeOfStatements) = nextStatementsList.last()
        return typeOfStatements.parse(manager.getTokens())
    }

    companion object {

        private val expressionEvaluatorV_1 = ExpressionType.makeExpressionEvaluatorV1_0()
        private val expressionEvaluatorV_1_1 = ExpressionType.makeExpressionEvaluatorV1_1()

        fun makeStatementParser(version: String): StatementParser {
            return when (version) {
                "1.0" -> createV1Parser()
                "1.1" -> createV1_1Parser()
                else -> throw Error("version not supported")
            }
        }

        private fun createV1Parser(): StatementParser {
            val statement = GenericStatementParser(
                listOf(
                    Pair("PRINT", PrintStatementParser(expressionEvaluatorV_1)),
                    Pair("", ExpressionStatementParser(expressionEvaluatorV_1))
                )
            )

            val declarationAssignationStatement = GenericStatementParser(
                listOf(
                    Pair("DECLARATION_KEYWORD", LetDeclarationParser(expressionEvaluatorV_1)),
                    Pair("", statement)
                )
            )

            return declarationAssignationStatement
        }

        private fun createV1_1Parser(): StatementParser {
            val statement = GenericStatementParser(
                listOf(
                    Pair("PRINT", PrintStatementParser(expressionEvaluatorV_1_1)),
                    Pair("IF", IfStatementParser(expressionEvaluatorV_1_1, ::createV1_1Parser)),
                    Pair("LEFT_BRACE", BlockStatementParser(::createV1_1Parser)),
                    Pair("", ExpressionStatementParser(expressionEvaluatorV_1_1))
                )
            )

            val declarationAssignationStatement = GenericStatementParser(
                listOf(
                    Pair("LET", LetDeclarationParser(expressionEvaluatorV_1_1)),
                    Pair("CONST", ConstDeclarationParser(expressionEvaluatorV_1_1)),
                    Pair("", statement)
                )
            )

            return declarationAssignationStatement
        }
    }
}
