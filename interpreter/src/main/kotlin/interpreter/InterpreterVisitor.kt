package interpreter

import environment.Environment
import nodes.Expression
import nodes.Statement
import utils.BinaryOperator
import utils.InputProvider
import utils.PrintScriptInputProvider
import visitor.Visitor
import visitor.VisitorResult

class InterpreterVisitor(private val environment: Environment = Environment(), val version: String = "1.1", val inputProvider: InputProvider = PrintScriptInputProvider()) :
    Visitor<VisitorResult> {

    val readInput = ""

    override fun visitExpression(statement: Statement.StatementExpression): VisitorResult {
        val newEnvironment = evaluateExpression(statement.value).second
        return Pair(StringBuilder(), newEnvironment)
    }

    override fun visitVariableStatement(statement: Statement.Variable): VisitorResult {
        val newEnvironment = if (statement.initializer != null) {
            val (newValue, env) = evaluateExpression(statement.initializer)
            val initializer = Expression.Literal(newValue, statement.position)
            val newVariable = Statement.Variable(
                designation = statement.designation,
                identifier = statement.identifier,
                initializer = initializer,
                dataType = statement.dataType,
                position = statement.position
            )
            env.define(newVariable)
        } else {
            environment.define(statement)
        }
        return Pair(StringBuilder(), newEnvironment)
    }

    override fun visitBlock(statement: Statement.BlockStatement): VisitorResult {
        var newEnvironment = Environment(enclosing = environment)
        val stringBuilder = StringBuilder()
        statement.listStm.forEach {
            val (_, newEnv) = it.accept(this)

            newEnvironment = newEnv
        }
        val env = newEnvironment.enclosing ?: throw IllegalArgumentException("Environment enclosing is null")
        return Pair(stringBuilder, env)
    }

    override fun visitIf(statement: Statement.IfStatement): VisitorResult {
        val (value, newEnvironment) = evaluateExpression(statement.condition)

        val booleanValue = when (value) {
            is Boolean -> value
            else -> throw IllegalArgumentException("Invalid expression for if statement: $value in ${statement.position}, expected boolean or variable")
        }

        return if (booleanValue) {
            statement.thenBranch.accept(this)
        } else {
            statement.elseBranch?.accept(this) ?: Pair(StringBuilder(), newEnvironment)
        }
    }

    override fun visitVariableExpression(expression: Expression.Variable): VisitorResult {
        val value = environment.getValue(expression.name)
        return Pair(value, environment)
    }

    override fun visitAssign(expression: Expression.Assign): VisitorResult {
        val (value, newScope) = evaluateExpression(expression.value)
        newScope.assign(expression.name, value as Expression)
        return Pair(value, newScope)
    }

    override fun visitBinary(exp: Expression.Binary): VisitorResult {
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

    override fun visitGrouping(expression: Expression.Grouping): VisitorResult {
        return expression.accept(this)
    }

    override fun visitLiteral(exp: Expression.Literal): VisitorResult {
        val value = exp.value
        return Pair(value, environment)
    }

    override fun visitUnary(expression: Expression.Unary): VisitorResult {
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

    override fun visitIdentifier(expression: Expression.IdentifierExpression): VisitorResult {
        return Pair(expression, environment)
    }

    override fun visitPrint(statement: Statement.Print): VisitorResult {
        val (value, _) = evaluateStatement(statement)
        println(value)
        return Pair(value, environment)
    }

    override fun visitReadInput(expression: Expression.ReadInput): VisitorResult {
        val input = readInput
        return Pair(input.toString().trim(), environment)
    }

    override fun visitReadEnv(expression: Expression.ReadEnv): VisitorResult {
        val keyResult = evaluateExpression(expression.value)
        val variableName = keyResult.first.toString()
        val envValue = environment.getValue(variableName)
        return Pair(envValue, environment)
    }

    fun evaluateExpression(expr: Expression): VisitorResult {
        return expr.accept(this)
    }

    private fun evaluateStatement(statement: Statement): VisitorResult {
        return statement.accept(this)
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
