package parser.statement

class MethodCallStatementValidator : StatementTypeValidator {
  override fun isValid(statement: Statement): StatementValResult {
    if (statement.content.size == 1) return StatementValResult(false, statement.content[0], "Expected an '(' after ${statement.content[0].value}")
    if (statement.content[1].type != "PUNCTUATION" || statement.content[1].value != "(") {
      return StatementValResult(
        false,
        statement.content[1],
        "Expected an '(' after ${statement.content[0].value}, got '${statement.content[1].value}' instead",
      )
    }
    if (statement.content[statement.content.size - 2].type != "PUNCTUATION" || statement.content[statement.content.size - 2].value != ")") {
      return StatementValResult(false, statement.content[statement.content.size - 2], "Did not close parenthesis with ')'")
    }
    if (statement.content[statement.content.size - 1].value != ";") {
      return StatementValResult(
        false,
        statement.content[statement.content.size - 1],
        "Expected a ';' at the end of the statement, got '${statement.content[statement.content.size - 1].value}' instead",
      )
    }
    return StatementValResult(true, null, null)
  }
}
