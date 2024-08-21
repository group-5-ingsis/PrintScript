package parser.statement

interface StatementType {
    fun isValid(statement : Statement) : StatementValResult
}
