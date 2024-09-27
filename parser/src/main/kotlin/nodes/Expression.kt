package nodes

import token.Position
import visitor.Visitor

sealed class Expression {
    abstract val expressionType: String
    abstract val position: Position

    abstract fun <T> accept(visitor: Visitor<T>): T

    data class Variable(val name: String, override val position: Position) : Expression() {
        override val expressionType = "VARIABLE_EXPRESSION"

        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitVariableExpression(this)
        }
    }
    data class Assign(val name: String, val value: Expression, override val position: Position) : Expression() {
        override val expressionType = "ASSIGNMENT_EXPRESSION"

        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitAssign(this)
        }
    }

    data class Binary(
        val left: Expression,
        val operator: String,
        val right: Expression,
        override val position: Position
    ) : Expression() {
        override val expressionType: String = "BINARY_EXPRESSION"

        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitBinary(this)
        }
    }

    data class Grouping(val expression: Expression, override val position: Position) : Expression() {
        override val expressionType: String = "GROUPING_EXPRESSION"

        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitGrouping(this)
        }
    }

    data class Literal(val value: Any?, override val position: Position) : Expression() {
        override val expressionType: String = "LITERAL_EXPRESSION"

        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitLiteral(this)
        }
    }

    data class Unary(val operator: String, val right: Expression, override val position: Position) : Expression() {
        override val expressionType: String = "UNARY_EXPRESSION"

        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitUnary(this)
        }
    }

    data class IdentifierExpression(val name: String, override val position: Position) : Expression() {
        override val expressionType: String = "IDENTIFIER"

        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitIdentifier(this)
        }
    }

    class ReadInput(
        override val position: Position,
        val value: Grouping
    ) : Expression() {
        override val expressionType: String = "READ_INPUT"
        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitReadInput(this)
        }
    }

    class ReadEnv(
        override val position: Position,
        val value: Grouping
    ) : Expression() {
        override val expressionType: String = "READ_ENV"

        override fun <T> accept(visitor: Visitor<T>): T {
            return visitor.visitReadEnv(this)
        }
    }
}
