package formatter

import composite.Node
import rules.FormattingRules
import visitor.ValueResolver
import visitor.Visitor

class FormattingVisitor(private val rules: FormattingRules) : Visitor {
    private val output = StringBuilder()

    fun getFormattedOutput(): String = output.toString()

    override fun visitAssignation(assignation: Node.Assignation) {
        val identifier = assignation.identifier.value
        val value = ValueResolver.resolveValue(assignation.value)

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
        val value = ValueResolver.resolveValue(assignationDeclaration.value)

        output.append("${applySpacesAroundAssignment()}$dataType $identifier = $value;")
        appendNewlineAfterSemicolon()
    }

    override fun visitMethodCall(methodCall: Node.Method) {
        val methodName = methodCall.identifier.value
        val arguments = methodCall.arguments.argumentsOfAnyTypes.joinToString(", ") { arg ->
            ValueResolver.resolveValue(arg)
                .toString()
        }

        output.append("${" ".repeat(rules.newlineBeforePrintln)}$methodName($arguments)")
    }

    private fun applySpacesAroundAssignment(): String {
        return if (rules.spaceAroundAssignment) " = " else "="
    }

    private fun appendNewlineAfterSemicolon() {
        output.append("\n")
    }
}
