package visitor

import environment.Environment
import nodes.Expression
import nodes.Statement

typealias statementVisitorResult = Pair<StringBuilder, Environment>

class StatementVisitor(val readInput: String? = null) {

    fun evaluateExpression(expr: Expression): VisitorResultExpressions {
        val visitor = ExpressionVisitor(readInput)
        return expr.accept(visitor)
    }

    fun visitBlockStm(statement: Statement.BlockStatement, environment: Environment, stringBuilder: StringBuilder): statementVisitorResult {
        var newEnvironment = Environment(enclosing = environment)
        var stB = StringBuilder(stringBuilder.toString())
        statement.listStm.forEach {
            val (newStringBuilder, newEnv) = it.accept(this, newEnvironment, stB)

            stB = newStringBuilder
            newEnvironment = newEnv
        }
        val env = newEnvironment.enclosing ?: throw IllegalArgumentException("Environment enclosing is null")
        return Pair(stB, env)
    }

    fun visitIfStm(
        statement: Statement.IfStatement,
        environment: Environment,
        stringBuilder: StringBuilder
    ): statementVisitorResult {
        val (value, newEnvironment) = evaluateExpression(statement.condition, environment)

        val newStringBuilder = StringBuilder(stringBuilder.toString())

        val booleanValue = when (value) {
            is Boolean -> value
            else -> throw IllegalArgumentException("Invalid expression for if statement: $value in ${statement.position}, expected boolean or variable")
        }

        return if (booleanValue) {
            statement.thenBranch.accept(this)
        } else {
            statement.elseBranch?.accept(this) ?: Pair(newStringBuilder, newEnvironment)
        }
    }

    fun visitPrintStm(
        statement: Statement.Print,
        environment: Environment,
        stringBuilder: StringBuilder
    ): statementVisitorResult {
        val newValue = evaluateExpression(statement.value)
        val printTarget = newValue.first

        val trimmedPrintTarget = printTarget.toString().trim().removeSurrounding("\"", "\"")

        if (stringBuilder.isEmpty()) {
            return Pair(StringBuilder(trimmedPrintTarget), environment)
        }

        stringBuilder.append("\n$trimmedPrintTarget")
        return Pair(stringBuilder, environment)
    }

    fun visitExpressionStm(statement: Statement.StatementExpression, stringBuilder: StringBuilder): statementVisitorResult {
        val newEnvironment = evaluateExpression(statement.value).second
        return Pair(stringBuilder, newEnvironment)
    }

    fun visitVariableStm(statement: Statement.Variable, environment: Environment, stringBuilder: StringBuilder): statementVisitorResult {
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
        return Pair(stringBuilder, newEnvironment)
    }
}
