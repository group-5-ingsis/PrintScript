package formatter

import nodes.StatementType
import rules.FormattingRules

class Formatter(private val parser: Iterator<StatementType>) {

    fun format(rules: FormattingRules): String {
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
