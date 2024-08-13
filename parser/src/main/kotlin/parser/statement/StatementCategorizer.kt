package parser.statement

import parser.statement.StatementType.Companion.allExistingStatements

class StatementCategorizer {

  fun categorize(statements: List<Statement>) : List<Statement> {
    val categorizedStatements = mutableListOf<Statement>()
    val newList = allExistingStatements.toList()

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
}