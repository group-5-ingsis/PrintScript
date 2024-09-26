package formatter

import nodes.Statement
import rules.FormattingRules

object Formatter {

    fun format(statement: Statement, rules: FormattingRules, version: String = "1.1"): String {
        val formattedResult = StringBuilder()
        val visitor = FormatterVisitor(rules, version)
        statement.accept(visitor)
        val formattedOutput = visitor.getFormattedOutput()
        formattedResult.append(formattedOutput)
        return formattedResult.toString()
    }
}
