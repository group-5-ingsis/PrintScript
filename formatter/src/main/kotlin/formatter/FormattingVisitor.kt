package formatter

import composite.Node
import rules.FormattingRules
import visitor.Visitor

class FormattingVisitor(private val rules: FormattingRules) : Visitor {
    private val output = StringBuilder()

    fun getFormattedOutput(): String = output.toString()

    override fun visitAssignation(assignation: Node.Assignation) {
        val identifier = assignation.identifier.value
        val value = resolveValue(assignation.value)

        output.append("${applySpacesAroundAssignment()}$identifier = $value;")
        appendNewlineAfterSemicolon()
    }

    override fun visitDeclaration(declaration: Node.Declaration) {
        val dataType = declaration.dataType.type
        val identifier = declaration.identifier

        output.append("${applySpacesAroundAssignment()}$dataType $identifier;")
        appendNewlineAfterSemicolon()
    }

    override fun visitAssignDeclare(assignationDeclaration: Node.AssignationDeclaration) {
        val dataType = assignationDeclaration.dataType.type
        val identifier = assignationDeclaration.identifier
        val value = resolveAssignableValue(assignationDeclaration.value)

        output.append("${applySpacesAroundAssignment()}$dataType $identifier = $value;")
        appendNewlineAfterSemicolon()
    }

    override fun visitMethodCall(methodCall: Node.Method) {
        val methodName = methodCall.identifier.value
        val arguments = methodCall.arguments.argumentsOfAnyTypes.joinToString(", ") { arg -> resolveValue(arg) }

        output.append("${" ".repeat(rules.newline_before_println)}$methodName($arguments)")
    }

    private fun applySpacesAroundAssignment(): String {
        return if (rules.space_around_assignment) " = " else "="
    }

    private fun appendNewlineAfterSemicolon() {
        output.append("\n")
    }

    private fun resolveValue(node: Node): String {
        return when (node) {
            is Node.GenericLiteral -> node.value
            is Node.Identifier -> node.value
            else -> throw IllegalArgumentException("Unsupported node type: ${node.nodeType}")
        }
    }

    private fun resolveAssignableValue(value: Node.AssignableValue): String {
        return when (value) {
            is Node.GenericLiteral -> value.value
            is Node.Identifier -> value.value
            is Node.BinaryOperations -> "${resolveValue(value.left)} ${value.symbol} ${resolveValue(value.right)}"
            else -> throw Exception("Implement other cases of AssignableValue types")
        }
    }

    override fun getVisitorFunction(nodeType: String): (Node) -> Unit {
        return when (nodeType) {
            "ASSIGNATION" -> { node ->
                visitAssignation(node as Node.Assignation)
            }

            "DECLARATION" -> { node ->
                visitDeclaration(node as Node.Declaration)
            }

            "ASSIGNATION_DECLARATION" -> { node ->
                visitAssignDeclare(node as Node.AssignationDeclaration)
            }

            "METHOD_CALL" -> { node ->
                visitMethodCall(node as Node.Method)
            }

            else -> {
                TODO("Not implemented yet")
            }
        }
    }
}
