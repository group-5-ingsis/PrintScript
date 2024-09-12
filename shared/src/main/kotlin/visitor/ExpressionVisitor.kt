package position.visitor

import nodes.Expression
import nodes.StatementType

class ExpressionVisitor(val readInput: String? = null) {

    private fun evaluateExpression(expr: Expression, environment: Environment): VisitorResultExpressions {
        return expr.acceptVisitor(this, environment)
    }

    private fun visitLiteralExp(exp: Expression.Literal, environment: Environment): VisitorResultExpressions {
        return Pair(exp.value, environment)
    }
    private fun visitGroupExp(exp: Expression.Grouping, environment: Environment): VisitorResultExpressions {
        return evaluateExpression(exp.expression, environment)
    }
    private fun visitUnaryExpr(exp: Expression.Unary, environment: Environment): Pair<Any?, Environment> {
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

    private fun visitBinaryExpr(exp: Expression.Binary, scope: Environment): Pair<Any?, Environment> {
        val (left, leftScope) = evaluateExpression(exp.left, scope)
        val (right, rigthScope) = evaluateExpression(exp.right, leftScope)

        fun checkTypesForOperation(left: Any?, right: Any?): Pair<Any?, Any?> {
            return when {
                left is Number && right is Number -> Pair(left, right)
                left is StatementType.Variable && right is StatementType.Variable -> {
                    Pair(
                        scope.get(left.identifier).initializer?.value,
                        scope.get(right.identifier).initializer?.value
                    )
                }
                left is StatementType.Variable && right is Number -> {
                    Pair(scope.get(left.identifier).initializer?.value, right)
                }
                left is StatementType.Variable && right is String -> {
                    Pair(scope.get(left.identifier).initializer?.value, right)
                }
                left is Number && right is StatementType.Variable -> {
                    Pair(left, scope.get(right.identifier).initializer?.value)
                }
                left is String && right is StatementType.Variable -> {
                    Pair(left, scope.get(right.identifier).initializer?.value)
                }

                left is String && right is String -> Pair(left, right)
                else -> throw IllegalArgumentException("Unsupported types for '${exp.operator}' operation: $left and $right")
            }
        }

        val (leftValue, rightValue) = checkTypesForOperation(left, right)

        return when (exp.operator) {
            "-" -> {
                if (leftValue != null && rightValue != null) {
                    checkNumberOperands(exp.operator, leftValue, rightValue)
                }
                val result = if (leftValue is Int && rightValue is Int) {
                    leftValue - rightValue
                } else {
                    (leftValue as Number).toDouble() - (rightValue as Number).toDouble()
                }
                Pair(result, rigthScope)
            }
            "/" -> {
                if (leftValue != null && rightValue != null) {
                    checkNumberOperands(exp.operator, leftValue, rightValue)
                }
                val result = if (leftValue is Int && rightValue is Int) {
                    leftValue / rightValue
                } else {
                    (leftValue as Number).toDouble() / (rightValue as Number).toDouble()
                }
                Pair(result, rigthScope)
            }
            "*" -> {
                if (leftValue != null && rightValue != null) {
                    checkNumberOperands(exp.operator, leftValue, rightValue)
                }
                val result = if (leftValue is Int && rightValue is Int) {
                    leftValue * rightValue
                } else {
                    (leftValue as Number).toDouble() * (rightValue as Number).toDouble()
                }
                Pair(result, rigthScope)
            }
            "+" -> {
                val result = when {
                    leftValue is Int && rightValue is Int -> leftValue + rightValue

                    leftValue is Number && rightValue is Number ->
                        leftValue.toDouble() + rightValue.toDouble()

                    leftValue is String && rightValue is String -> leftValue + rightValue

                    leftValue is Number && rightValue is String -> leftValue.toString() + rightValue.trim().removeSurrounding("\"")

                    leftValue is String && rightValue is Number -> leftValue.trim().removeSurrounding("\"") + rightValue.toString()

                    leftValue is StatementType.Variable && rightValue is String -> {
                        scope.get(leftValue.identifier).initializer?.value.toString() + rightValue.trim().removeSurrounding("\"")
                    }
                    rightValue is StatementType.Variable && leftValue is String -> {
                        leftValue.trim().removeSurrounding("\"") + scope.get(rightValue.identifier).initializer?.value.toString()
                    }

                    else -> throw IllegalArgumentException("Unsupported types for PLUS operation: ${leftValue!!::class.simpleName} and ${rightValue!!::class.simpleName}, must be numbers or strings")
                }
                Pair(result, rigthScope)
            }

            ">" -> {
                val result = when {
                    leftValue is Int && rightValue is Int -> leftValue > rightValue
                    leftValue is Number && rightValue is Number -> leftValue.toDouble() > rightValue.toDouble()
                    leftValue is String && rightValue is String -> leftValue > rightValue
                    else -> throw IllegalArgumentException("Unsupported types for GREATER THAN operation: ${leftValue!!::class.simpleName} and ${rightValue!!::class.simpleName}")
                }
                Pair(result, rigthScope)
            }
            ">=" -> {
                val result = when {
                    leftValue is Int && rightValue is Int -> leftValue >= rightValue
                    leftValue is Number && rightValue is Number -> leftValue.toDouble() >= rightValue.toDouble()
                    leftValue is String && rightValue is String -> leftValue >= rightValue
                    else -> throw IllegalArgumentException("Unsupported types for GREATER THAN OR EQUAL operation: ${leftValue!!::class.simpleName} and ${rightValue!!::class.simpleName}")
                }
                Pair(result, rigthScope)
            }
            "<" -> {
                val result = when {
                    leftValue is Int && rightValue is Int -> leftValue < rightValue
                    leftValue is Number && rightValue is Number -> leftValue.toDouble() < rightValue.toDouble()
                    leftValue is String && rightValue is String -> leftValue < rightValue
                    else -> throw IllegalArgumentException("Unsupported types for LESS THAN operation: ${leftValue!!::class.simpleName} and ${rightValue!!::class.simpleName}")
                }
                Pair(result, rigthScope)
            }
            "<=" -> {
                val result = when {
                    leftValue is Int && rightValue is Int -> leftValue <= rightValue
                    leftValue is Number && rightValue is Number -> leftValue.toDouble() <= rightValue.toDouble()
                    leftValue is String && rightValue is String -> leftValue <= rightValue
                    else -> throw IllegalArgumentException("Unsupported types for LESS THAN OR EQUAL operation: ${leftValue!!::class.simpleName} and ${rightValue!!::class.simpleName}")
                }
                Pair(result, rigthScope)
            }
            "==" -> {
                val result = when {
                    leftValue is Int && rightValue is Int -> leftValue == rightValue
                    leftValue is Number && rightValue is Number -> leftValue.toDouble() == rightValue.toDouble()
                    leftValue is String && rightValue is String -> leftValue == rightValue
                    else -> throw IllegalArgumentException("Unsupported types for EQUAL operation: ${leftValue!!::class.simpleName} and ${rightValue!!::class.simpleName}")
                }
                Pair(result, rigthScope)
            }
            "!=" -> {
                val result = when {
                    leftValue is Int && rightValue is Int -> leftValue != rightValue
                    leftValue is Number && rightValue is Number -> leftValue.toDouble() != rightValue.toDouble()
                    leftValue is String && rightValue is String -> leftValue != rightValue
                    else -> throw IllegalArgumentException("Unsupported types for NOT EQUAL operation: ${leftValue!!::class.simpleName} and ${rightValue!!::class.simpleName}")
                }
                Pair(result, rigthScope)
            }
            else -> throw IllegalArgumentException("Unsupported operator: ${exp.operator}")
        }
    }

    private fun visitVariableExp(exp: Expression.Variable, scope: Environment): VisitorResultExpressions {
        val expressionName = exp.name
        val name = scope.get(expressionName)
        return Pair(name, scope)
    }

    private fun visitAssignExpr(exp: Expression.Assign, scope: Environment): VisitorResultExpressions {
        val (value, newScope) = evaluateExpression(exp.value, scope)
        val newScope2 = newScope.assign(exp.name, value)
        return Pair(value, newScope2)
    }
    fun getVisitorFunctionForExpression(expressionType: String): (Expression, Environment) -> VisitorResultExpressions {
        return when (expressionType) {
            "READ_INPUT" -> { expr, env ->
                if (expr is Expression.ReadInput) visitReadInput(expr, env) else throw IllegalArgumentException("Invalid expression type for READ_INPUT")
            }
            "READ_ENV" -> { expr, env ->
                if (expr is Expression.ReadEnv) visitReadEnv(expr, env) else throw IllegalArgumentException("Invalid expression type for READ_ENV")
            }
            "ASSIGNMENT_EXPRESSION" -> { expr, env ->
                if (expr is Expression.Assign) visitAssignExpr(expr, env) else throw IllegalArgumentException("Invalid expression type for ASSIGNMENT_EXPRESSION")
            }
            "VARIABLE_EXPRESSION" -> { expr, env ->
                if (expr is Expression.Variable) visitVariableExp(expr, env) else throw IllegalArgumentException("Invalid expression type for VARIABLE_EXPRESSION")
            }
            "BINARY_EXPRESSION" -> { expr, env ->
                if (expr is Expression.Binary) visitBinaryExpr(expr, env) else throw IllegalArgumentException("Invalid expression type for BINARY_EXPRESSION")
            }
            "GROUPING_EXPRESSION" -> { expr, env ->
                if (expr is Expression.Grouping) visitGroupExp(expr, env) else throw IllegalArgumentException("Invalid expression type for GROUPING_EXPRESSION")
            }
            "LITERAL_EXPRESSION" -> { expr, env ->
                if (expr is Expression.Literal) visitLiteralExp(expr, env) else throw IllegalArgumentException("Invalid expression type for LITERAL_EXPRESSION")
            }
            "UNARY_EXPRESSION" -> { expr, env ->
                if (expr is Expression.Unary) visitUnaryExpr(expr, env) else throw IllegalArgumentException("Invalid expression type for UNARY_EXPRESSION")
            }
            "IDENTIFIER" -> { expr, env ->
                if (expr is Expression.IdentifierExpression) visitIdentifierExp(expr, env) else throw IllegalArgumentException("Invalid expression type for UNARY_EXPRESSION")
            }
            else -> throw IllegalArgumentException("Unsupported expression type: $expressionType")
        }
    }

    private fun visitReadInput(expr: Expression.ReadInput, env: Environment): VisitorResultExpressions {
        val input = readInput
        return Pair(input.toString().trim(), env)
    }

    private fun visitReadEnv(expr: Expression.ReadEnv, env: Environment): VisitorResultExpressions {
        val key = expr.value

        val value = key.expression.value

        val envValue = env.get(value.toString())

        return Pair(envValue, env)
    }

    private fun visitIdentifierExp(exp: Expression.IdentifierExpression, environment: Environment): VisitorResultExpressions {
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
