package formatter

import nodes.Statement
import rules.FormattingRules

object Formatter {

    fun format(statement: Statement, rules: FormattingRules, version: String = "1.1"): String {
        val visitor = FormatterVisitor(rules, version)
        return statement.accept(visitor)
    }
}
