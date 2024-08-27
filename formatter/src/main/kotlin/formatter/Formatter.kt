package formatter

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import composite.Node
import rules.FormattingRules
import java.io.File

object Formatter {

    fun format(ast: Node, rulesFile: File): String {
        val rules = loadRules(rulesFile)
        return formatNode(ast, rules)
    }

    private fun loadRules(file: File): FormattingRules {
        val mapper = YAMLMapper()
        return mapper.readValue(file, FormattingRules::class.java)
    }

    private fun formatNode(node: Node, rules: FormattingRules): String {
        val visitor = FormattingVisitor(rules)
        node.accept(visitor)
        return visitor.getFormattedOutput()
    }
}
