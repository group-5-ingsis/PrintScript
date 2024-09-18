package formatter

import nodes.StatementType
import rules.FormattingRules

object Formatter {

    val formattedResult = StringBuilder()

    fun format(statement: StatementType, rules: FormattingRules, version: String = "1.1"): String {
        val visitor = FormatterVisitor(rules, version)
        statement.accept(visitor)
        val formattedOutput = visitor.getFormattedOutput()
        formattedResult.append(formattedOutput)
        return formattedResult.toString()
    }
}
