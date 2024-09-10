package formatter

import nodes.StatementType
import rules.FormattingRules

object Formatter {

    fun format(parser: Iterator<StatementType>, rules: FormattingRules, version: String): String {
        val output = StringBuilder()

        while (parser.hasNext()) {
            val astNode = parser.next()
            val formattingVisitor = FormatterVisitor(rules)
            astNode.accept(formattingVisitor)
            output.append(formattingVisitor.getFormattedOutput())
        }

        return output.toString()
    }
}
