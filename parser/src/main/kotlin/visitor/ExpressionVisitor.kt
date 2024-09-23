package visitor

import environment.Environment
import nodes.Expression
import nodes.Statement

class ExpressionVisitor(val readInput: String? = null) : Visitor<VisitorResultExpressions> {

    val environment = Environment()

    fun evaluateExpression(expr: Expression): VisitorResultExpressions {
        return expr.accept(this)
    }

    override fun visitLiteral(exp: Expression.Literal): VisitorResultExpressions {
        return Pair(exp.value, environment)
    }

    override fun visitGrouping(expression: Expression.Grouping): VisitorResultExpressions {
        return expression.accept(this)
    }

    override fun visitUnary(expression: Expression.Unary): VisitorResultExpressions {
        val rightObject = expression.right.accept(this)
        return when (expression.operator) {
            "-" -> {
                val result = -convertToDouble(rightObject.first)
                Pair(result, environment)
            }
            "!" -> {
                val result = !isTruthy(rightObject.first)
                Pair(result, environment)
            }
            else -> throw IllegalArgumentException("Unsupported types for ${expression.operator} in Unary operation: $rightObject")
        }
    }

    override fun visitBinary(exp: Expression.Binary): VisitorResultExpressions {
        val (left, _) = evaluateExpression(exp.left)
        val (right, rightEnv) = evaluateExpression(exp.right)

        return when {
            (left is Number && right is Number) -> BinaryOperator.solveNumberAndNumber(left, right, exp.operator) to rightEnv
            (left is String && right is String) -> BinaryOperator.solveStringAndString(left, right, exp.operator) to rightEnv
            (left is String && right is Number) -> BinaryOperator.solveStringAndNumber(left, right, exp.operator) to rightEnv
            (left is Number && right is String) -> BinaryOperator.solveNumberAndString(left, right, exp.operator) to rightEnv
            else -> throw IllegalArgumentException("Unsupported operand types: ${left!!::class} and ${right!!::class}")
        }
    }

    override fun visitVariable(exp: Expression.Variable): VisitorResultExpressions {
        val value = environment.getValue(exp.name)
        return Pair(value, environment)
    }

    override fun visitAssign(exp: Expression.Assign): VisitorResultExpressions {
        val (value, newScope) = evaluateExpression(exp.value)
        newScope.assign(exp.name, value as Expression)
        return Pair(value, newScope)
    }

    override fun visitReadInput(expr: Expression.ReadInput): VisitorResultExpressions {
        val input = readInput
        return Pair(input.toString().trim(), environment)
    }

    override fun visitReadEnv(expr: Expression.ReadEnv): VisitorResultExpressions {
        val keyResult = evaluateExpression(expr.value)
        val variableName = keyResult.first.toString()
        val envValue = environment.getValue(variableName)
        return Pair(envValue, environment)
    }

    override fun visitIdentifier(exp: Expression.IdentifierExpression): VisitorResultExpressions {
        return Pair(exp, environment)
    }

    override fun visitPrint(statement: Statement.Print): VisitorResultExpressions {
        val (value, _) = evaluateExpression(statement.expression)
        println(value) // Output the value
        return Pair(value, environment)
    }

    override fun visitExpression(statement: Statement.StatementExpression): VisitorResultExpressions {
        return evaluateExpression(statement.expression, environment)
    }

    override fun visitVariable(statement: Statement.Variable): VisitorResultExpressions {
        val (value, newScope) = evaluateExpression(statement.initializer, environment)
        newScope.assign(statement.name, value)
        return Pair(value, newScope)
    }

    override fun visitBlock(statement: Statement.BlockStatement): VisitorResultExpressions {
        var currentEnv = environment
        for (stm in statement.statements) {
            val result = evaluateStatement(stm, currentEnv)
            currentEnv = result.second // Update environment with the result's new scope
        }
        return Pair(Unit, currentEnv)
    }

    override fun visitIf(statement: Statement.IfStatement): VisitorResultExpressions {
        val conditionValue = evaluateExpression(statement.condition, environment)
        return if (isTruthy(conditionValue.first)) {
            evaluateStatement(statement.thenBranch, environment)
        } else {
            statement.elseBranch?.let { evaluateStatement(it, environment) } ?: Pair(Unit, environment)
        }
    }

    // Implement evaluateStatement if needed
    private fun evaluateStatement(statement: Statement, environment: Environment): VisitorResultExpressions {
        return statement.accept(this, environment)
    }

    private fun isTruthy(value: Any?): Boolean {
        if (value == null) return false
        if (value is Boolean) return value
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
