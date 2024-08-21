package parser.statement

object StatementManager {
    fun categorize(statements : List<Statement>) : List<Statement> {
        val toReturn = mutableListOf<Statement>()
        for (statement in statements) {
            toReturn.add(categorizeStatement(statement))
        }
        TODO()
    }

    private fun categorizeStatement(statement: Statement): Statement {
        when (statement.content[0].type) {
            "DECLARATION_KEYWORD" ->
                if (!DeclarationStatementValidator().isValid(statement).isInvalid) {
                    statement.statementType = "DECLARATION"
                    return statement
                }


            "" -> AssignmentStatementValidator().isValid(statement)
            else -> throw UnsupportedOperationException("Unexpected statement")
        }
        TODO()
    }
}
