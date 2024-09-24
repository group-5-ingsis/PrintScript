package formatter

import rules.FormattingRules

object Formatter {

    fun format(statement: StatementType, rules: FormattingRules, version: String = "1.1"): String {
        val formattedResult = StringBuilder()
        val visitor = FormatterVisitor(rules, version)
        statement.accept(visitor)
        val formattedOutput = visitor.getFormattedOutput()
        formattedResult.append(formattedOutput)
        return formattedResult.toString()
    }
}
