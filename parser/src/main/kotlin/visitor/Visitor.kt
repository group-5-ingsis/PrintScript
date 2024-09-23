package visitor

import nodes.Expression
import nodes.Statement

interface Visitor<R> {
    fun visitExpression(statement: Statement.StatementExpression): R
    fun visitVariableStatement(statement: Statement.Variable): R
    fun visitVariableExpression(expression: Expression.Variable): R
    fun visitBlock(statement: Statement.BlockStatement): R
    fun visitIf(statement: Statement.IfStatement): R
    fun visitAssign(expression: Expression.Assign): R
    fun visitBinary(expression: Expression.Binary): R
    fun visitGrouping(expression: Expression.Grouping): R
    fun visitLiteral(expression: Expression.Literal): R
    fun visitUnary(expression: Expression.Unary): R
    fun visitIdentifier(expression: Expression.IdentifierExpression): R
    fun visitPrint(statement: Statement.Print): R
    fun visitReadInput(expression: Expression.ReadInput): R
    fun visitReadEnv(expression: Expression.ReadEnv): R
}
