package formatter

import composite.Node
import rules.FormattingRules

object Formatter {

    fun format(node: Node, rules: FormattingRules): String {
        val visitor = FormattingVisitor(rules)
        node.accept(visitor)
        return visitor.getFormattedOutput()
    }
}
