package formatter

import composite.Node
import rules.FormattingRules

object Formatter {

    fun format(ast: Node, rules: FormattingRules): String {
        return formatNode(ast, rules)
    }

    private fun formatNode(node: Node, rules: FormattingRules): String {
        val visitor = FormattingVisitor(rules)
        node.accept(visitor)
        return visitor.getFormattedOutput()
    }
}
