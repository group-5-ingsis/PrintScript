package parser.statement

interface StatementType {
    fun isType(statement: Statement): Boolean
}