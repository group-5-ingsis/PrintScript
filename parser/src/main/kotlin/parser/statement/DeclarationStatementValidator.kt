package parser.statement

class DeclarationStatementValidator : StatementTypeValidator {
  override fun isValid(statement: Statement): StatementValResult {
    if (statement.content.size == 1) {
      return StatementValResult(
        false,
        statement.content[0],
        "Expected an 'identifier' after ${statement.content[0].value}",
      )
    }
    if (statement.content[1].type != "IDENTIFIER") {
      return StatementValResult(
        false,
        statement.content[1],
        "Expected an 'identifier' after ${statement.content[0].value}, " +
          "got '${statement.content[1].value}' instead",
      )
    }
    if (statement.content[2].type != "PUNCTUATION" || statement.content[2].value != ":") {
      return StatementValResult(
        false,
        statement.content[2],
        "Expected a ':' after '${statement.content[0].value} ${statement.content[1].value}', " +
          "but got '${statement.content[2].value}' instead",
      )
    }
    if (statement.content[3].type != "VARIABLE_TYPE") {
      return StatementValResult(
        false,
        statement.content[3],
        "Expected a 'variable type' like 'String' or 'Number' after '${statement.content[2].value}', " +
          "but got '${statement.content[3].value}' instead",
      )
    }
    if (statement.content[4].value != ";" && statement.content[4].value != "=") {
      return StatementValResult(
        false,
        statement.content[4],
        "Expected a ';' or a '=' after '${statement.content[2].value} ${statement.content[3].value}', " +
          "but got a '${statement.content[4].value}'",
      )
    }
    return StatementValResult(true, null, null)
  }
}
