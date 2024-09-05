package formatter

import Environment
import nodes.Expression
import nodes.StatementType
import parser.syntactic.SyntacticParser
import position.visitor.ExpressionVisitor
import rules.FormattingRules

object NodeFormatter {

    fun format(node: SyntacticParser.RootNode, formattingRules: FormattingRules): String {
        return node.getChildren().joinToString("\n") { statement ->
            formatStatement(statement, formattingRules)
        }
    }

    private fun formatStatement(statement: StatementType, rules: FormattingRules): String {
        return when (statement) {
            is StatementType.Print -> formatPrintStm(statement, rules)
            is StatementType.StatementExpression -> formatExpressionStm(statement)
            is StatementType.Variable -> formatVariableStm(statement, rules)
            else -> throw IllegalArgumentException("Unsupported statement type: ${statement::class.simpleName}")
        }
    }

    private fun formatPrintStm(statement: StatementType.Print, rules: FormattingRules): String {
        val value = evaluateExpression(statement.value)
        val newline = "\n".repeat(rules.newlineBeforePrintln)
        return "$newline print $value;"
    }

    private fun formatExpressionStm(statement: StatementType.StatementExpression): String {
        val expression = evaluateExpression(statement.value)
        return "$expression;"
    }

    private fun formatVariableStm(statement: StatementType.Variable, rules: FormattingRules): String {
        val initializer = statement.initializer?.let {
            val value = evaluateExpression(it)
            formatAssignment(rules, statement.identifier, value)
        } ?: statement.identifier
        return "$initializer;"
    }

    private fun evaluateExpression(expr: Expression): String {
        val visitor = ExpressionVisitor()
        val (value, _) = expr.acceptVisitor(visitor, Environment())
        return value.toString()
    }

    private fun formatAssignment(rules: FormattingRules, identifier: String, value: String): String {
        val space = if (rules.spaceAroundAssignment) " " else ""
        return "$identifier$space=$space$value"
    }
}
