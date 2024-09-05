package formatter

import parser.syntactic.SyntacticParser

class Formatter(private val parser: Iterator<SyntacticParser.RootNode>) {

    fun format(): String {
        val formattedOutput = StringBuilder()

        while (parser.hasNext()) {
            val astNode = parser.next()
            val formattedNode = formatASTNode(astNode)
            formattedOutput.append(formattedNode)
        }

        return formattedOutput.toString()
    }

    private fun formatASTNode(node: SyntacticParser.RootNode): String {
        return NodeFormatter.format(node)
    }
}
