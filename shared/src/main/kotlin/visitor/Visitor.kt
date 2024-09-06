package position.visitor

import nodes.StatementType

interface Visitor {
    fun visitPrintStm(statement: StatementType.Print)
    fun visitExpressionStm(statement: StatementType.StatementExpression)
    fun visitVariableStm(statement: StatementType.Variable)
}
