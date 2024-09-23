package visitor

import environment.Environment
import nodes.Expression
import nodes.StatementType
import position.visitor.VisitorResultExpressions

typealias statementVisitorResult = Pair<StringBuilder, environment.Environment>

class StatementVisitor(val readInput: String? = null) {

    fun evaluateExpression(expr: Expression, scope: Environment): VisitorResultExpressions {
        val visitor = ExpressionVisitor(readInput)
        return expr.acceptVisitor(visitor, scope)
    }

    fun visitBlockStm(statement: StatementType.BlockStatement, environment: Environment, stringBuilder: StringBuilder): statementVisitorResult {
        var newEnvironment = Environment(enclosing = environment)
        var stB = StringBuilder(stringBuilder.toString())
        statement.listStm.forEach {
            val (newStringBuilder, newEnv) = it.acceptVisitor(this, newEnvironment, stringBuilder)

            stB = newStringBuilder
            newEnvironment = newEnv
        }
        val env = newEnvironment.enclosing ?: throw IllegalArgumentException("Environment enclosing is null")
        return Pair(stB, env)
    }

    fun visitIfStm(
        statement: StatementType.IfStatement,
        environment: Environment,
        stringBuilder: StringBuilder
    ): statementVisitorResult {
        val (value, newEnvironment) = evaluateExpression(statement.condition, environment)

        val newStringBuilder = StringBuilder(stringBuilder.toString())

        val booleanValue = when (value) {
            is Boolean -> value
            else -> throw IllegalArgumentException("Invalid expression for if statement: $value" + " in " + statement.position.toString() + ", expected boolean or variable")
        }

        return if (booleanValue) {
            statement.thenBranch.acceptVisitor(this, newEnvironment, newStringBuilder)
        } else {
            statement.elseBranch?.acceptVisitor(this, newEnvironment, newStringBuilder) ?: Pair(newStringBuilder, newEnvironment)
        }
    }

    fun visitPrintStm(
        statement: StatementType.Print,
        environment: Environment,
        stringBuilder: StringBuilder
    ): statementVisitorResult {
        val value = statement.value
        val newValue = evaluateExpression(value, environment)
        val printTarget = newValue.first

        val trimmedPrintTarget = printTarget.toString().trim().removeSurrounding("\"", "\"")

        if (stringBuilder.toString() == "") {
            return Pair(StringBuilder(trimmedPrintTarget), environment)
        }

        stringBuilder.append("\n" + trimmedPrintTarget)

        return Pair(stringBuilder, environment)
    }

    fun visitExpressionStm(statement: StatementType.StatementExpression, environment: Environment, stringBuilder: StringBuilder): statementVisitorResult {
        val newEnvironment = evaluateExpression(statement.value, environment).second
        return Pair(stringBuilder, newEnvironment)
    }

    fun visitVariableStm(statement: StatementType.Variable, environment: Environment, stringBuilder: StringBuilder): statementVisitorResult {
        val newEnvironment = if (statement.initializer != null) {
            val (newValue, env) = evaluateExpression(statement.initializer, environment)
            val initializer = Expression.Literal(newValue, statement.position)
            val newVariable = StatementType.Variable(
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
