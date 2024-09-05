package formatter

import parser.syntactic.SyntacticParser
import rules.FormattingRules

class Formatter(private val parser: Iterator<SyntacticParser.RootNode>) {

    fun format(rules: FormattingRules): String {
        val formattedOutput = StringBuilder()

        while (parser.hasNext()) {
            val astNode = parser.next()
            val formattedNode = formatASTNode(astNode, rules)
            formattedOutput.append(formattedNode)
        }

        return formattedOutput.toString()
    }

    private fun formatASTNode(node: SyntacticParser.RootNode, rules: FormattingRules): String {
        return NodeFormatter.format(node, rules)
    }
}
