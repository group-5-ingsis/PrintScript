package linter

import nodes.Expression
import nodes.StatementType
import position.visitor.Visitor

class LinterVisitor(private val rules: Map<String, Any>) : Visitor {
    override fun visitPrintStm(statement: StatementType.Print) {
        TODO("Not yet implemented")
    }

    override fun visitExpressionStm(statement: StatementType.StatementExpression) {
        TODO("Not yet implemented")
    }

    override fun visitVariableStm(statement: StatementType.Variable) {
        TODO("Not yet implemented")
    }

    override fun visitVariable(expression: Expression.Variable) {
        TODO("Not yet implemented")
    }

    override fun visitAssign(expression: Expression.Assign) {
        TODO("Not yet implemented")
    }

    override fun visitBinary(expression: Expression.Binary) {
        TODO("Not yet implemented")
    }

    override fun visitGrouping(expression: Expression.Grouping) {
        TODO("Not yet implemented")
    }

    override fun visitLiteral(expression: Expression.Literal) {
        TODO("Not yet implemented")
    }

    override fun visitUnary(expression: Expression.Unary) {
        TODO("Not yet implemented")
    }

    override fun visitIdentifier(expression: Expression.IdentifierExpression) {
        TODO("Not yet implemented")
    }
}
