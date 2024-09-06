package nodes

import Environment
import position.Position
import position.visitor.StatementVisitor
import position.visitor.Visitor

sealed class StatementType {

    abstract val statementType: String
    abstract val position: Position

    fun acceptVisitor(visitor: StatementVisitor, environment: Environment): Environment {
        val func = visitor.getVisitorFunctionForStatement(statementType)
        return func(this, environment)
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

        init {
            DataTypeManager.checkDataType(dataType)
            DataTypeManager.checkVariableDec(designation)
            DataTypeManager.checkVariableName(identifier)
        }
    }
}
