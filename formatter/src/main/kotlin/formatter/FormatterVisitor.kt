package formatter

import nodes.StatementType
import position.visitor.Visitor
import rules.FormattingRules
import rules.RuleApplier

class FormatterVisitor(private val rules: FormattingRules) : Visitor {

    private val output = StringBuilder()
    private val ruleApplier = RuleApplier(rules)

    fun getFormattedOutput(): String {
        return output.toString()
    }

    override fun visitPrintStm(statement: StatementType.Print) {
        repeat(rules.newlineBeforePrintln) {
            output.append("\n")
        }
        output.append("print(${statement.value});\n")
    }

    override fun visitExpressionStm(statement: StatementType.StatementExpression) {
        val expression = statement.value
        return expression.accept(this)
    }

    override fun visitVariableStm(statement: StatementType.Variable) {
        val spaceBeforeColon = if (rules.spaceBeforeColon) " " else ""
        val spaceAfterColon = if (rules.spaceAfterColon) " " else ""
        val spaceAroundAssignment = if (rules.spaceAroundAssignment) " " else ""

        output.append("var ${statement.identifier}")
        output.append("$spaceBeforeColon:${spaceAfterColon}${statement.dataType}")
        output.append("$spaceAroundAssignment=${spaceAroundAssignment}${statement.initializer};\n")
    }
}
