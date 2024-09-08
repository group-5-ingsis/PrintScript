package position.visitor

import Environment
import VisitorResultExpressions
import nodes.Expression
import nodes.StatementType

typealias statementVisitorResult = Pair<StringBuilder, Environment>

class StatementVisitor {
    private fun evaluateExpression(expr: Expression, scope: Environment): VisitorResultExpressions {
        val visitor = ExpressionVisitor()
        return expr.acceptVisitor(visitor, scope)
    }

    fun getVisitorFunctionForStatement(statementType: String): (StatementType, Environment, StringBuilder) -> statementVisitorResult {
        return when (statementType) {
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
        val nullValue = null
        val newEnvironment = if (statement.initializer != null) {
            val (newValue, env) = evaluateExpression(statement.initializer, environment)
            env.define(statement.identifier, newValue, statement.designation)
        } else {
            environment.define(statement.identifier, nullValue, statement.designation)
        }
        return Pair(stringBuilder, newEnvironment)
    }
}
