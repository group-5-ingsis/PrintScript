package parser.statement

object StatementManager {
    fun categorize(statements : List<Statement>) : List<Statement> {
        val statementList = mutableListOf<Statement>()
        for (statement in statements) {
            statementList.add(categorizeStatement(statement))
        }
        return statementList
    }

    private fun categorizeStatement(statement: Statement): Statement {
        when (statement.content[0].type) {
            "DECLARATION_KEYWORD" -> {
              val validationResult = DeclarationStatementValidator().isValid(statement)
              if (validationResult.isValid) {
                statement.statementType = getDeclarationType(statement)
                return statement
              } else {
                SyntacticErrorHandler.handle(validationResult)
              }
            }
            "IDENTIFIER" -> {
              val validationResult = AssignmentStatementValidator().isValid(statement)
              if (validationResult.isValid) {
                statement.statementType = "Assignation"
                return statement
              } else {
                SyntacticErrorHandler.handle(validationResult)
              }
            }
            "PREDEF_METHOD" -> {
              val validationResult = MethodCallStatementValidator().isValid(statement)
              if (validationResult.isValid) {
                statement.statementType = "MethodCall"
                return statement
              } else {
                SyntacticErrorHandler.handle(validationResult)
              }
            }
            else -> throw UnsupportedOperationException("Unexpected statement")
        }
      throw UnsupportedOperationException("Unexpected statement")
    }

  private fun getDeclarationType(statement: Statement): String {
    if (statement.content[4].type == "ASSIGNATION" || statement.content[4].value == "=") {
      val subStatement = Statement(listOf(statement.content[1]) + (statement.content.subList(4, statement.content.size)), "Unknown")
      val validationResult = AssignmentStatementValidator().isValid(subStatement)
      if (validationResult.isValid) {
        statement.statementType = "Assignation"
        return "AssignDeclare"
      } else {
        SyntacticErrorHandler.handle(validationResult)
        return "Declaration"
      }
    } else {
      return "Declaration"
    }
  }
}
