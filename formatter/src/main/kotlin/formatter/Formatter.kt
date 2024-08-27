package formatter

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import composite.Node
import rules.FormattingRules
import java.io.File

object Formatter {

    fun format(ast: Node, rulesFile: String): String {
        val rules = loadRules(rulesFile)
        return formatNode(ast, rules)
    }

    private fun loadRules(file: String): FormattingRules {
        val mapper = YAMLMapper()
        return mapper.readValue(File(file), FormattingRules::class.java)
    }

    private fun formatNode(node: Node, rules: FormattingRules): String {
        val visitor = FormattingVisitor(rules)
        node.acceptVisitor(visitor)
        return visitor.getFormattedOutput()
    }
}
