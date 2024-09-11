package nodes

import Environment
import VisitorResultExpressions
import position.Position
import position.visitor.ExpressionVisitor
import position.visitor.Visitor

sealed class Expression {
    abstract val expressionType: String
    abstract val position: Position
    abstract val value: Any?

    fun acceptVisitor(visitor: ExpressionVisitor, environment: Environment): VisitorResultExpressions {
        val func = visitor.getVisitorFunctionForExpression(expressionType)
        return func(this, environment)
    }

    abstract fun accept(visitor: Visitor)

    // There are complex expression that are variables exp: Position(x, y).y = 5, refers to this
    data class Variable(val name: String, override val position: Position, override val value: Any? = null) : Expression() {
        override val expressionType = "VARIABLE_EXPRESSION"
        override fun accept(visitor: Visitor) {
            return visitor.visitVariable(this)
        }
    }
    data class Assign(val name: String, override val value: Expression, override val position: Position) : Expression() {
        override val expressionType = "ASSIGNMENT_EXPRESSION"
        override fun accept(visitor: Visitor) {
            return visitor.visitAssign(this)
        }
    }

    data class Binary(val left: Expression, val operator: String, val right: Expression, override val position: Position, override val value: Any? = null) : Expression() {
        override val expressionType: String = "BINARY_EXPRESSION"
        override fun accept(visitor: Visitor) {
            return visitor.visitBinary(this)
        }
    }

    data class Grouping(val expression: Expression, override val position: Position, override val value: Any? = null) : Expression() {
        override val expressionType: String = "GROUPING_EXPRESSION"
        override fun accept(visitor: Visitor) {
            return visitor.visitGrouping(this)
        }
    }

    data class Literal(override val value: Any?, override val position: Position) : Expression() {
        override val expressionType: String = "LITERAL_EXPRESSION"
        override fun accept(visitor: Visitor) {
            return visitor.visitLiteral(this)
        }
    }

    data class Unary(val operator: String, val right: Expression, override val position: Position, override val value: Any? = null) : Expression() {
        override val expressionType: String = "UNARY_EXPRESSION"
        override fun accept(visitor: Visitor) {
            return visitor.visitUnary(this)
        }
    }

    data class IdentifierExpression(val name: String, override val position: Position, override val value: Any? = null) : Expression() {
        override val expressionType: String = "IDENTIFIER"
        override fun accept(visitor: Visitor) {
            return visitor.visitIdentifier(this)
        }
    }
}
