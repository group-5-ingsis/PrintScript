package nodes

import token.Position
import visitor.Visitor

sealed class Expression {
    abstract val expressionType: String
    abstract val position: Position

    abstract fun <R> accept(visitor: Visitor<R>): R

    data class Variable(val name: String, override val position: Position) : Expression() {
        override val expressionType = "VARIABLE_EXPRESSION"

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVariable(this)
        }
    }
    data class Assign(val name: String, val value: Expression, override val position: Position) : Expression() {
        override val expressionType = "ASSIGNMENT_EXPRESSION"

        override fun <R> accept(visitor: Visitor<R>): R {
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

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitBinary(this)
        }
    }

    data class Grouping(val expression: Expression, override val position: Position) : Expression() {
        override val expressionType: String = "GROUPING_EXPRESSION"

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitGrouping(this)
        }
    }

    data class Literal(val value: Any?, override val position: Position) : Expression() {
        override val expressionType: String = "LITERAL_EXPRESSION"

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitLiteral(this)
        }
    }

    data class Unary(val operator: String, val right: Expression, override val position: Position) : Expression() {
        override val expressionType: String = "UNARY_EXPRESSION"

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitUnary(this)
        }
    }

    data class IdentifierExpression(val name: String, override val position: Position) : Expression() {
        override val expressionType: String = "IDENTIFIER"

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitIdentifier(this)
        }
    }

    class ReadInput(
        override val position: Position,
        val value: Grouping,
        val message: String
    ) : Expression() {
        override val expressionType: String = "READ_INPUT"

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitReadInput(this)
        }
    }

    class ReadEnv(override val position: Position, val value: Grouping) : Expression() {
        override val expressionType: String = "READ_ENV"

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitReadEnv(this)
        }
    }
}
