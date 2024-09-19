package position.visitor

import Environment
import nodes.Expression

class ExpressionVisitor(val readInput: String? = null) {

    fun evaluateExpression(expr: Expression, environment: Environment): VisitorResultExpressions {
        return expr.acceptVisitor(this, environment)
    }

    fun visitLiteralExp(exp: Expression.Literal, environment: Environment): VisitorResultExpressions {
        return Pair(exp.value, environment)
    }
    fun visitGroupExp(exp: Expression.Grouping, environment: Environment): VisitorResultExpressions {
        return evaluateExpression(exp.expression, environment)
    }
    fun visitUnaryExpr(exp: Expression.Unary, environment: Environment): Pair<Any?, Environment> {
        val rightObject = evaluateExpression(exp.right, environment)
        return when (exp.operator) {
            "-" -> {
                val result = -convertToDouble(rightObject)
                Pair(result, environment)
            }
            "!" -> {
                val result = !isTruthy(rightObject)
                Pair(result, environment)
            }
            else -> throw IllegalArgumentException("Unsupported types for ${exp.operator} in Unary operation: $rightObject")
        }
    }

    fun visitBinaryExpr(exp: Expression.Binary, scope: Environment): Pair<Any?, Environment> {
        val (left, leftScope) = evaluateExpression(exp.left, scope)
        val (right, rigthScope) = evaluateExpression(exp.right, leftScope)

        return when {
            (left is Number && right is Number) -> solveNumberAndNumber(left, right, exp.operator) to rigthScope
            (left is String && right is String) -> solveStringAndString(left, right, exp.operator) to rigthScope
            (left is String && right is Number) -> solveStringAndNumber(left, right, exp.operator) to rigthScope
            (left is Number && right is String) -> solveNumberAndString(left, right, exp.operator) to rigthScope
            else -> throw IllegalArgumentException("Unsupported operand types: ${left!!::class} and ${right!!::class}")
        }
    }

    fun solveStringAndString(left: String, right: String, operator: String): String {
        return when (operator) {
            "+" -> left + right
            else -> throw IllegalArgumentException("Unsupported string operation: $operator")
        }
    }

    fun solveStringAndNumber(left: String, right: Number, operator: String): String {
        return when (operator) {
            "+" -> left.removeSurrounding("\"") + right.toString()
            else -> throw IllegalArgumentException("Unsupported operation between String and Number: $operator")
        }
    }

    fun solveNumberAndString(left: Number, right: String, operator: String): String {
        return when (operator) {
            "+" -> left.toString() + right.removeSurrounding("\"")
            else -> throw IllegalArgumentException("Unsupported operation between Number and String: $operator")
        }
    }

    fun solveNumberAndNumber(left: Number, right: Number, operator: String): Number {
        val leftValue = if (left is Int) left else left.toDouble()
        val rightValue = if (right is Int) right else right.toDouble()

        return when (operator) {
            "+" -> if (leftValue is Int && rightValue is Int) leftValue + rightValue else (leftValue.toDouble() + rightValue.toDouble())
            "-" -> if (leftValue is Int && rightValue is Int) leftValue - rightValue else (leftValue.toDouble() - rightValue.toDouble())
            "*" -> if (leftValue is Int && rightValue is Int) leftValue * rightValue else (leftValue.toDouble() * rightValue.toDouble())
            "/" -> if (leftValue is Int && rightValue is Int) leftValue / rightValue else (leftValue.toDouble() / rightValue.toDouble())
            else -> throw IllegalArgumentException("Unsupported number operation: $operator")
        }
    }

    fun visitVariableExp(exp: Expression.Variable, scope: Environment): VisitorResultExpressions {
        val expressionName = exp.name
        val name = scope.getValue(expressionName)

        return Pair(name, scope)
    }

    fun visitAssignExpr(exp: Expression.Assign, scope: Environment): VisitorResultExpressions {
        val (value, newScope) = evaluateExpression(exp.value, scope)
        val newScope2 = newScope.assign(exp.name, exp.value)
        return Pair(value, newScope2)
    }

    fun visitReadInput(expr: Expression.ReadInput, env: Environment): VisitorResultExpressions {
        val input = readInput
        return Pair(input.toString().trim(), env)
    }

    fun visitReadEnv(expr: Expression.ReadEnv, env: Environment): VisitorResultExpressions {
        val key = expr.value

        val result = evaluateExpression(key.expression, env)

        val variableName = result.first
        val environment = result.second

        val envValue = environment.getValue(variableName.toString())

        return Pair(envValue, env)
    }

    fun visitIdentifierExp(exp: Expression.IdentifierExpression, environment: Environment): VisitorResultExpressions {
        return Pair(exp, environment)
    }

    private fun checkNumberOperands(operator: String, left: Any, right: Any) {
        if (left is Number && right is Number) return
        throw RuntimeException("error in operator: $operator , $left and $right  must be numbers.")
    }

    private fun isTruthy(thing: Any?): Boolean {
        if (thing == null) return false
        if (thing is Boolean) return thing
        return true
    }

    private fun convertToDouble(value: Any?): Double {
        return when (value) {
            is Double -> value
            is Float -> value.toDouble()
            is Int -> value.toDouble()
            is Long -> value.toDouble()
            is String -> value.toDoubleOrNull() ?: throw IllegalArgumentException("Cannot convert String to Double: $value")
            else -> throw IllegalArgumentException("Unsupported type: $value")
        }
    }
}
