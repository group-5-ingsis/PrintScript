package parser.statement

object StatementManager {
    val allExistingStatements: MutableSet<StatementType> = mutableSetOf()

    init {
        addStatements()
    }


    private fun addStatements() {
        allExistingStatements.add(
            StatementType(
                listOf(
                    TokensNamesForStatements.SingleName("DECLARATION_KEYWORD"),
                    TokensNamesForStatements.SingleName("IDENTIFIER"),
                    TokensNamesForStatements.SingleName("PUNCTUATION"),
                    TokensNamesForStatements.SingleName("VARIABLE_TYPE"),
                    TokensNamesForStatements.SingleName("ASSIGNMENT"),
                    TokensNamesForStatements.MultipleNames(listOf("NUMBER", "STRING", "IDENTIFIER")),
                    TokensNamesForStatements.SingleName("PUNCTUATION"),
                ), "AssignDeclare"
            )
        )

        allExistingStatements.add(
            StatementType(
                listOf(
                    TokensNamesForStatements.SingleName("DECLARATION_KEYWORD"),
                    TokensNamesForStatements.SingleName("IDENTIFIER"),
                    TokensNamesForStatements.SingleName("PUNCTUATION"),
                    TokensNamesForStatements.SingleName("VARIABLE_TYPE")
                ),
                "Declaration"
            )
        )

        allExistingStatements.add(
            StatementType(
                listOf(
                    TokensNamesForStatements.SingleName("IDENTIFIER"),
                    TokensNamesForStatements.SingleName("ASSIGNMENT"),
                    TokensNamesForStatements.MultipleNames(listOf("NUMBER", "STRING", "IDENTIFIER"))
                ),
                "Assignation"
            )
        )
        allExistingStatements.add(
            StatementType(
                listOf(
                    TokensNamesForStatements.SingleName("PREDEF_METHOD"),
                    TokensNamesForStatements.SingleName("PUNCTUATION"),
                    TokensNamesForStatements.MultipleNames(listOf("NUMBER", "STRING", "IDENTIFIER")),
                    TokensNamesForStatements.SingleName("PUNCTUATION"),
                    TokensNamesForStatements.SingleName("PUNCTUATION"),
                ), "MethodCall"
            )
        )

        allExistingStatements.add(StatementType(listOf(), "Unknown"))
    }

    fun categorize(statements: List<Statement>) : List<Statement> {
        val categorizedStatements = mutableListOf<Statement>()
        val newList = StatementManager.allExistingStatements.toList()

        for (statement in statements) {
            for (allowedStatement in newList) {
                val isType = allowedStatement.isType(statement)
                if (isType) {
                    statement.statementType = allowedStatement.name
                    categorizedStatements.add(statement)
                    break
                }
            }
        }
        return categorizedStatements
    }


    sealed class TokensNamesForStatements {
        data class SingleName(val value: String) : TokensNamesForStatements()
        data class MultipleNames(val values: List<String>) : TokensNamesForStatements()
    }


}