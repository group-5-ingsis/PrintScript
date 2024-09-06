package position.visitor

import nodes.Expression
import nodes.StatementType

interface Visitor {
    fun visitPrintStm(statement: StatementType.Print)
    fun visitExpressionStm(statement: StatementType.StatementExpression)
    fun visitVariableStm(statement: StatementType.Variable)
    fun visitVariable(expression: Expression.Variable)
    fun visitAssign(expression: Expression.Assign)
    fun visitBinary(expression: Expression.Binary)
    fun visitGrouping(expression: Expression.Grouping)
    fun visitLiteral(expression: Expression.Literal)
    fun visitUnary(expression: Expression.Unary)
    fun visitIdentifier(expression: Expression.IdentifierExpression)
}
