package parser.statement

class DeclarationStatementValidator : StatementType {
    override fun isValid(statement: Statement): StatementValResult {
        if (statement.content[1].type != "IDENTIFIER") {
            return StatementValResult(false, statement.content[1], "Expected an 'identifier' type, got a '${statement.content[1].type}' type")
        }
        if (statement.content[2].type != "PUNCTUATION" || statement.content[2].value != ":") {
            return StatementValResult(false, statement.content[2], "Expected a ':' after ${statement.content[0]} ${statement.content[1]}, but got a '${statement.content[2].value}'")
        }
        if (statement.content[3].type != "VARIABLE_TYPE") {
            return StatementValResult(false, statement.content[3], "Expected a 'variable type' like 'String' or 'Number' after '${statement.content[2]}', but got '${statement.content[3].value}' instead")
        }
        return StatementValResult(true, null, null)
    }
}
