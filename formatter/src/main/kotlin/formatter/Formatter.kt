package formatter

import nodes.StatementType
import rules.FormattingRules


object Formatter {

    fun format(ast: StatementType, rules: FormattingRules): String {
        return formatNode(ast, rules)
    }

    private fun formatNode(node: StatementType, rules: FormattingRules): String {
        val visitor = FormattingVisitor(rules)
        node.acceptVisitor(visitor)
        return visitor.getFormattedOutput()
    }
}

