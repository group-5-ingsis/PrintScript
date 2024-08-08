package parser

import parser.statement.AssignationDeclarationStatement
import parser.statement.Statement
import parser.statement.StatementType

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
        }
      }
    }
    return categorizedStatements
  }

  private fun getAllowedStatements(): List<StatementType> {
    val statements: MutableList<StatementType> = mutableListOf()
    statements.add(AssignationDeclarationStatement())
    return statements
  }

}