package nodes

import Environment
import position.Position
import position.visitor.StatementVisitor

sealed class StatementType {

    abstract val statementType: String
    abstract val position: Position

    fun acceptVisitor(visitor: StatementVisitor, environment: Environment): Environment {
        val func = visitor.getVisitorFunctionForStatement(statementType)
        return func(this, environment)
    }

    class Print(val value: Expression.Grouping, override val position: Position) : StatementType() {
        override val statementType: String = "PRINT"
    }

    class StatementExpression(val value: Expression, override val position: Position) : StatementType() {
        override val statementType: String = "STATEMENT_EXPRESSION"
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
        init {
            DataTypeManager.checkDataType(dataType)
            DataTypeManager.checkVariableDec(designation)
            DataTypeManager.checkVariableName(identifier)
        }
    }
}
