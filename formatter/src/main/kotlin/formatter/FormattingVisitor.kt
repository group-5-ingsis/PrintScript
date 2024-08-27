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

        val newLine = "$identifier${applySpacesAroundAssignment()}$value;"
        output.append(newLine)
        appendNewlineAfterSemicolon()
    }

    override fun visitDeclaration(declaration: Node.Declaration) {
        val dataType = declaration.dataType.type
        val identifier = declaration.identifier

        output.append("${applySpacesAroundAssignment()}$dataType $identifier;")
        appendNewlineAfterSemicolon()
    }

    override fun visitAssignDeclare(assignationDeclaration: Node.AssignationDeclaration) {
        val dataType = setDataType(assignationDeclaration.dataType)
        val assignationType = assignationDeclaration.kindVariableDeclaration
        val identifier = assignationDeclaration.identifier
        val value = ValueResolver.resolveValue(assignationDeclaration.value)

        val spacesAroundAssignment = applySpacesAroundAssignment()
        val newLine = "$assignationType $identifier${applySpaceForColon()}$dataType$spacesAroundAssignment$value;"
        output.append(newLine)
        appendNewlineAfterSemicolon()
    }

    private fun setDataType(dataType: Node.DataType): String {
        if (dataType.type == "NUMBER") {
            return "Number"
        }
        return "String"
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
        val spaceAroundAssignment = rules.spaceAroundAssignment
        return if (spaceAroundAssignment) " = " else "="
    }

    private fun applySpaceForColon(): String {
        val spaceBeforeColon = rules.spaceBeforeColon
        val spaceAfterColon = rules.spaceAfterColon

        return when {
            spaceBeforeColon && spaceAfterColon -> " : "
            spaceBeforeColon && !spaceAfterColon -> " :"
            !spaceBeforeColon && spaceAfterColon -> ": "
            else -> ":"
        }
    }

    private fun appendNewlineAfterSemicolon() {
        output.append("\n")
    }
}
