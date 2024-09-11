package position.visitor

import nodes.Expression
import nodes.StatementType

typealias statementVisitorResult = Pair<StringBuilder, Environment>

class StatementVisitor {
    private fun evaluateExpression(expr: Expression, scope: Environment): VisitorResultExpressions {
        val visitor = ExpressionVisitor()
        return expr.acceptVisitor(visitor, scope)
    }

    private fun visitBlockStm(statement: StatementType.BlockStatement, environment: Environment, stringBuilder: StringBuilder): statementVisitorResult {
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

    private fun visitIfStm(statement: StatementType.IfStatement, environment: Environment, stringBuilder: StringBuilder): statementVisitorResult {
        val (value, newEnvironment) = evaluateExpression(statement.condition, environment)
        if (value !is StatementType.Variable) {
            throw Error("Expression is not a variable")
        }
        val newStringBuilder = StringBuilder(stringBuilder.toString())
        if (value.initializer?.value !is Boolean) {
            throw IllegalArgumentException("Invalid value for if statement: $value" + "in " + statement.position.toString() + " expected boolean")
        }
        return if (value.initializer.value is Boolean) {
            statement.thenBranch.acceptVisitor(this, newEnvironment, newStringBuilder)
        } else {
            statement.elseBranch?.acceptVisitor(this, newEnvironment, newStringBuilder) ?: Pair(newStringBuilder, newEnvironment)
        }
    }

    fun getVisitorFunctionForStatement(statementType: String): (StatementType, Environment, StringBuilder) -> statementVisitorResult {
        return when (statementType) {
            "IF_STATEMENT" -> { statement, env, sb ->
                if (statement is StatementType.IfStatement) {
                    visitIfStm(statement, env, sb)
                } else {
                    throw IllegalArgumentException("Invalid statement type for IF_STATEMENT: ${statement::class.simpleName}")
                }
            }
            "BLOCK_STATEMENT" -> { statement, env, sb ->
                if (statement is StatementType.BlockStatement) {
                    visitBlockStm(statement, env, sb)
                } else {
                    throw IllegalArgumentException("Invalid statement type for BLOCK_STATEMENT: ${statement::class.simpleName}")
                }
            }
            "PRINT" -> { statement, env, sb ->
                if (statement is StatementType.Print) {
                    visitPrintStm(statement, env, sb)
                } else {
                    throw IllegalArgumentException("Invalid statement type for PRINT: ${statement::class.simpleName}")
                }
            }

            "STATEMENT_EXPRESSION" -> { statement, env, sb ->
                if (statement is StatementType.StatementExpression) {
                    visitExpressionStm(statement, env, sb)
                } else {
                    throw IllegalArgumentException("Invalid statement type for STATEMENT_EXPRESSION: ${statement::class.simpleName}")
                }
            }

            "VARIABLE_STATEMENT" -> { statement, env, sb ->
                if (statement is StatementType.Variable) {
                    visitVariableStm(statement, env, sb)
                } else {
                    throw IllegalArgumentException("Invalid statement type for VARIABLE_STATEMENT: ${statement::class.simpleName}")
                }
            }

            else -> {
                throw IllegalArgumentException("Unsupported statement type: $statementType")
            }
        }
    }

    private fun visitPrintStm(statement: StatementType.Print, environment: Environment, stringBuilder: StringBuilder): statementVisitorResult {
        val value = evaluateExpression(statement.value, environment)
        stringBuilder.append("\n${value.first}\n")
        return Pair(stringBuilder, environment)
    }

    private fun visitExpressionStm(statement: StatementType.StatementExpression, environment: Environment, stringBuilder: StringBuilder): statementVisitorResult {
        val newEnvironment = evaluateExpression(statement.value, environment).second
        return Pair(stringBuilder, newEnvironment)
    }

    private fun visitVariableStm(statement: StatementType.Variable, environment: Environment, stringBuilder: StringBuilder): statementVisitorResult {
        val newEnvironment = if (statement.initializer != null) {
            val (newValue, env) = evaluateExpression(statement.initializer, environment)
            val newVariable = StatementType.Variable(
                designation = statement.designation,
                identifier = statement.identifier,
                initializer = Expression.Literal(newValue, statement.position),
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
