package formatter

import Environment
import nodes.Expression
import nodes.StatementType
import parser.syntactic.SyntacticParser
import position.visitor.ExpressionVisitor
import rules.FormattingRules

object NodeFormatter {

    fun format(node: SyntacticParser.RootNode, formattingRules: FormattingRules): String {
        return
    }

    private fun evaluateExpression(expr: Expression): String {
        val visitor = ExpressionVisitor()
        val (value, _) = expr.acceptVisitor(visitor)
        return value.toString()
    }

    fun getFormatterFunctionForStatement(
        statementType: String,
        rules: FormattingRules
    ): (StatementType, Environment) -> String {
        return when (statementType) {
            "PRINT" -> { statement, env ->
                if (statement is StatementType.Print) {
                    formatPrintStm(statement, env, rules)
                } else {
                    throw IllegalArgumentException("Invalid statement type for PRINT: ${statement::class.simpleName}")
                }
            }

            "STATEMENT_EXPRESSION" -> { statement, env ->
                if (statement is StatementType.StatementExpression) {
                    formatExpressionStm(statement, env, rules)
                } else {
                    throw IllegalArgumentException("Invalid statement type for STATEMENT_EXPRESSION: ${statement::class.simpleName}")
                }
            }

            "VARIABLE_STATEMENT" -> { statement, env ->
                if (statement is StatementType.Variable) {
                    formatVariableStm(statement, env, rules)
                } else {
                    throw IllegalArgumentException("Invalid statement type for VARIABLE_STATEMENT: ${statement::class.simpleName}")
                }
            }

            else -> {
                throw IllegalArgumentException("Unsupported statement type: $statementType")
            }
        }
    }

    private fun formatPrintStm(statement: StatementType.Print, environment: Environment, rules: FormattingRules): String {
        val value = evaluateExpression(statement.value, environment)
        val newline = "\n".repeat(rules.newlineBeforePrintln)
        return "$newline print $value;"
    }

    private fun formatExpressionStm(statement: StatementType.StatementExpression, environment: Environment, rules: FormattingRules): String {
        val expression = evaluateExpression(statement.value, environment)
        return "$expression;"
    }

    private fun formatVariableStm(statement: StatementType.Variable, environment: Environment, rules: FormattingRules): String {
        val initializer = if (statement.initializer != null) {
            val value = evaluateExpression(statement.initializer!!, environment)
            formatAssignment(rules, statement.identifier, value)
        } else {
            statement.identifier
        }
        return "$initializer;"
    }

    private fun formatAssignment(rules: FormattingRules, identifier: String, value: String): String {
        val space = if (rules.spaceAroundAssignment) " " else ""
        return "$identifier$space=${space}$value"
    }
}
