package parser.statement

interface StatementTypeValidator {
    fun isValid(statement: Statement): StatementValResult
}
