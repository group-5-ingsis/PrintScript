package parser.statement

class AssignmentStatementValidator : StatementTypeValidator {
  override fun isValid(statement: Statement): StatementValResult {
    if (statement.content.size == 1) return StatementValResult(false, statement.content[0], "Expected an '=' after ${statement.content[0].value}")
    if (statement.content[1].value != "=") {
      return StatementValResult(false, statement.content[1], "Expected an '=', but got a '${statement.content[1].value}' instead")
    }
    return StatementValResult(true, null, null)
  }
}
