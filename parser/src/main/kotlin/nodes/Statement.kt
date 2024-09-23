package nodes

import token.Position
import visitor.Visitor

sealed class Statement {

    abstract val statementType: String
    abstract val position: Position

    abstract fun <R> accept(visitor: Visitor<R>): R

    class BlockStatement(override val position: Position, val listStm: List<Statement>) : Statement() {
        override val statementType: String = "BLOCK_STATEMENT"

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitBlockStm(this)
        }
    }

    class IfStatement(override val position: Position, val condition: Expression, val thenBranch: Statement, val elseBranch: Statement?) : Statement() {
        override val statementType: String = "IF_STATEMENT"

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitIf(this)
        }
    }

    class Print(val value: Expression.Grouping, override val position: Position) : Statement() {
        override val statementType: String = "PRINT"

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitPrint(this)
        }
    }

    class StatementExpression(val value: Expression, override val position: Position) : Statement() {
        override val statementType: String = "STATEMENT_EXPRESSION"

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitExpression(this)
        }
    }

    class Variable(
        val designation: String,
        val identifier: String,
        val initializer: Expression?,
        val dataType: String,
        override val position: Position
    ) : Statement() {
        override val statementType: String = "VARIABLE_STATEMENT"

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVariable(this)
        }
    }
}
