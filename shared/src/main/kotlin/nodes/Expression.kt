package nodes

import Environment
import position.Position
import position.visitor.ExpressionVisitor
import position.visitor.Visitor
import position.visitor.VisitorResultExpressions

sealed class Expression {
    abstract val expressionType: String
    abstract val position: Position


    abstract fun acceptVisitor(visitor: ExpressionVisitor, environment: Environment): VisitorResultExpressions

    abstract fun accept(visitor: Visitor)

    // There are complex expression that are variables exp: Position(x, y).y = 5, refers to this
    data class Variable(val name: String, override val position: Position) : Expression() {
        override val expressionType = "VARIABLE_EXPRESSION"
        override fun acceptVisitor(visitor: ExpressionVisitor, environment: Environment): VisitorResultExpressions {
            return visitor.visitVariableExp(this, environment)
        }

        override fun accept(visitor: Visitor) {
            return visitor.visitVariable(this)
        }
    }
    data class Assign(val name: String, val value: Expression, override val position: Position) : Expression() {
        override val expressionType = "ASSIGNMENT_EXPRESSION"
        override fun acceptVisitor(visitor: ExpressionVisitor, environment: Environment): VisitorResultExpressions {
            return visitor.visitAssignExpr(this, environment)
        }

        override fun accept(visitor: Visitor) {
            return visitor.visitAssign(this)
        }
    }

    data class Binary(
        val left: Expression,
        val operator: String,
        val right: Expression,
        override val position: Position,
    ) : Expression() {
        override val expressionType: String = "BINARY_EXPRESSION"
        override fun acceptVisitor(visitor: ExpressionVisitor, environment: Environment): VisitorResultExpressions {
            return visitor.visitBinaryExpr(this, environment)
        }

        override fun accept(visitor: Visitor) {
            return visitor.visitBinary(this)
        }
    }

    data class Grouping(val expression: Expression, override val position: Position) : Expression() {
        override val expressionType: String = "GROUPING_EXPRESSION"
        override fun acceptVisitor(visitor: ExpressionVisitor, environment: Environment): VisitorResultExpressions {
            return visitor.visitGroupExp(this, environment)
        }

        override fun accept(visitor: Visitor) {
            return visitor.visitGrouping(this)
        }
    }

    data class Literal(val value: Any?, override val position: Position) : Expression() {
        override val expressionType: String = "LITERAL_EXPRESSION"
        override fun acceptVisitor(visitor: ExpressionVisitor, environment: Environment): VisitorResultExpressions {
            return visitor.visitLiteralExp(this, environment)
        }

        override fun accept(visitor: Visitor) {
            return visitor.visitLiteral(this)
        }
    }

    data class Unary(val operator: String, val right: Expression, override val position: Position) : Expression() {
        override val expressionType: String = "UNARY_EXPRESSION"
        override fun acceptVisitor(visitor: ExpressionVisitor, environment: Environment): VisitorResultExpressions {
            return visitor.visitUnaryExpr(this, environment)
        }

        override fun accept(visitor: Visitor) {
            return visitor.visitUnary(this)
        }
    }

    data class IdentifierExpression(val name: String, override val position: Position) : Expression() {
        override val expressionType: String = "IDENTIFIER"
        override fun acceptVisitor(visitor: ExpressionVisitor, environment: Environment): VisitorResultExpressions {
            return visitor.visitIdentifierExp(this, environment)
        }

        override fun accept(visitor: Visitor) {
            return visitor.visitIdentifier(this)
        }
    }

    class ReadInput(
        override val position: Position,
        val value: Grouping,
        val message: String
    ) : Expression() {
        override val expressionType: String = "READ_INPUT"
        override fun acceptVisitor(visitor: ExpressionVisitor, environment: Environment): VisitorResultExpressions {
            return visitor.visitReadInput(this, environment)
        }

        override fun accept(visitor: Visitor) {
            return visitor.visitReadInput(this)
        }
    }

    class ReadEnv(override val position: Position, val value: Grouping) : Expression() {
        override val expressionType: String = "READ_ENV"
        override fun acceptVisitor(visitor: ExpressionVisitor, environment: Environment): VisitorResultExpressions {
            return visitor.visitReadEnv(this, environment)
        }

        override fun accept(visitor: Visitor) {
            return visitor.visitReadEnv(this)
        }
    }
}
