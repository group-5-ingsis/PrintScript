package parser

import parser.statement.AssignationStatement
import parser.statement.Statement
import parser.statement.StatementType

class StatementCategorizer {

  private fun getAllowedStatements(): List<StatementType> {
    val statements: MutableList<StatementType> = mutableListOf()
    statements.add(AssignationStatement())
    return statements
  }

  fun categorize(statements: List<Statement>) : List<Statement> {

    val allowedStatements = getAllowedStatements()
    val categorizedStatements = mutableListOf<Statement>()

    for (statement in statements) {
      for (allowedStatement in allowedStatements) {
        if (allowedStatement.isType(statement)) {
          statement.statementType = allowedStatement
          categorizedStatements.add(statement)
        }
      }
    }
    return categorizedStatements
  }

}