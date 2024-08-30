package nodes

import position.Position
import visitor.NodeVisitor

sealed class Expression {
    abstract val expressionType: String
    abstract val position: Position

    fun acceptVisitor(visitor: NodeVisitor): Any? {
        val func = visitor.getVisitorFunctionForExpression(expressionType)
        return func(this)
    }

    // There are complex expression that are variables exp: Position(x, y).y = 5, refers to this
    data class Variable(val name: String, override val position: Position) : Expression() {
        override val expressionType = "VARIABLE_EXPRESSION"
    }
    data class Assign(val name: String, val value: Expression, override val position: Position) : Expression() {
        override val expressionType = "ASSIGNMENT_EXPRESSION"
    }

    data class Binary(val left: Expression, val operator: String, val right: Expression, override val position: Position) : Expression() {
        override val expressionType: String = "BINARY_EXPRESSION"
    }

    data class Grouping(val expression: Expression, override val position: Position) : Expression() {
        override val expressionType: String = "GROUPING_EXPRESSION"
    }

    data class Literal(val value: Any?, override val position: Position) : Expression() {
        override val expressionType: String = "LITERAL_EXPRESSION"
    }

    data class Unary(val operator: String, val right: Expression, override val position: Position) : Expression() {
        override val expressionType: String = "UNARY_EXPRESSION"
    }

    data class IdentifierExpression(val name: String, override val position: Position) : Expression() {
        override val expressionType: String = "IDENTIFIER"
    }
}
