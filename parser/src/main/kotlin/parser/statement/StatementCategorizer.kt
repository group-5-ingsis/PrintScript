package parser.statement

class StatementCategorizer {

  fun categorize(statements: List<Statement>) : List<Statement> {

    val allowedStatements = getAllowedStatements()
    val categorizedStatements = mutableListOf<Statement>()

    for (statement in statements) {
      for (allowedStatement in allowedStatements) {
        val isType = allowedStatement.isType(statement)
        if (isType) {
          statement.statementType = allowedStatement
          categorizedStatements.add(statement)
          break
        }
      }
    }
    
    return categorizedStatements
  }

  private fun getAllowedStatements(): List<StatementType> {
    val statements: MutableList<StatementType> = mutableListOf()
    statements.add(AssignationDeclarationStatement())
    statements.add(AssignationStatement())
    statements.add(DeclarationStatement())
    return statements
  }

}