package position.visitor

import Environment
import VisitorResultExpressions
import nodes.Expression
import nodes.StatementType

class StatementVisitor {
    private fun evaluateExpression(expr: Expression, scope: Environment): VisitorResultExpressions {
        val visitor = ExpressionVisitor()
        return expr.acceptVisitor(visitor, scope)
    }

    fun getVisitorFunctionForStatement(statementType: String): (StatementType, Environment) -> Environment {
        return when (statementType) {
            "PRINT" -> { statement, env ->
                if (statement is StatementType.Print) {
                    visitPrintStm(statement, env)
                } else {
                    throw IllegalArgumentException("Invalid statement type for PRINT: ${statement::class.simpleName}")
                }
            }

            "STATEMENT_EXPRESSION" -> { statement, env ->
                if (statement is StatementType.StatementExpression) {
                    visitExpressionStm(statement, env)
                } else {
                    throw IllegalArgumentException("Invalid statement type for STATEMENT_EXPRESSION: ${statement::class.simpleName}")
                }
            }

            "VARIABLE_STATEMENT" -> { statement, env ->
                if (statement is StatementType.Variable) {
                    visitVariableStm(statement, env)
                } else {
                    throw IllegalArgumentException("Invalid statement type for VARIABLE_STATEMENT: ${statement::class.simpleName}")
                }
            }

            else -> {
                throw IllegalArgumentException("Unsupported statement type: $statementType")
            }
        }
    }
    private fun visitPrintStm(statement: StatementType.Print, environment: Environment): Environment {
        val value = evaluateExpression(statement.value, environment)
        println(value.first)
        return environment
    }
    private fun visitExpressionStm(statement: StatementType.StatementExpression, environment: Environment): Environment {
        return evaluateExpression(statement.value, environment).second
    }
    private fun visitVariableStm(statement: StatementType.Variable, environment: Environment): Environment {
        val nullValue = null
        if (statement.initializer != null) {
            val (newValue, newEnvironment) = evaluateExpression(statement.initializer, environment)
            return newEnvironment.define(statement.identifier, newValue, statement.designation)
        }
        return environment.define(statement.identifier, nullValue, statement.designation)
    }
}
