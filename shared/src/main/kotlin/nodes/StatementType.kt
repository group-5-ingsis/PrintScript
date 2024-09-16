package nodes

import position.Position
import position.visitor.Environment
import position.visitor.StatementVisitor
import position.visitor.Visitor
import position.visitor.statementVisitorResult

sealed class StatementType {

    abstract val statementType: String
    abstract val position: Position

    class BlockStatement(override val position: Position, val listStm: List<StatementType>) : StatementType() {
        override val statementType: String = "BLOCK_STATEMENT"
        override fun accept(visitor: Visitor) {
            return visitor.visitBlockStm(this)
        }
    }
    class IfStatement(override val position: Position, val condition: Expression, val thenBranch: StatementType, val elseBranch: StatementType?) : StatementType() {
        override val statementType: String = "IF_STATEMENT"
        override fun accept(visitor: Visitor) {
            return visitor.visitIfStm(this)
        }
    }

    fun acceptVisitor(visitor: StatementVisitor, environment: Environment, sb: StringBuilder): statementVisitorResult {
        val func = visitor.getVisitorFunctionForStatement(statementType)
        return func(this, environment, sb)
    }

    abstract fun accept(visitor: Visitor)

    class Print(val value: Expression.Grouping, override val position: Position) : StatementType() {
        override val statementType: String = "PRINT"
        override fun accept(visitor: Visitor) {
            return visitor.visitPrintStm(this)
        }
    }

    class StatementExpression(val value: Expression, override val position: Position) : StatementType() {
        override val statementType: String = "STATEMENT_EXPRESSION"
        override fun accept(visitor: Visitor) {
            return visitor.visitExpressionStm(this)
        }
    }

    /**
     * `Variable` represents a variable declaration assignation or just declaration
     *
     * @param identifier the name of the variable
     * @param initializer the expression assigned to the identifier, if its just declaration, its null
     * @param dataType the type declarative of the variable
     * @param designation is const, let, etc, how is declarative
     */
    class Variable(
        val designation: String,
        val identifier: String,
        val initializer: Expression?,
        val dataType: String,
        override val position: Position
    ) : StatementType() {
        override val statementType: String = "VARIABLE_STATEMENT"
        override fun accept(visitor: Visitor) {
            return visitor.visitVariableStm(this)
        }
    }
}
