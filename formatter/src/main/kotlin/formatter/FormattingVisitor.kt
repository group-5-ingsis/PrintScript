package formatter

import composite.Node
import rules.FormattingRules
import rules.RuleApplier
import visitor.ValueResolver
import visitor.Visitor

class FormattingVisitor(private val rules: FormattingRules) : Visitor {
    private val output = StringBuilder()
    private val ruleApplier = RuleApplier(rules)

    fun getFormattedOutput(): String = output.toString()

    override fun visitAssignation(assignation: Node.Assignation) {
        val identifier = assignation.identifier.value
        val value = ValueResolver.resolveValue(assignation.value)

        val newLine = "$identifier${ruleApplier.applySpacesAroundAssignment()}$value;"
        output.append(newLine)
        output.append("\n")
    }

    override fun visitDeclaration(declaration: Node.Declaration) {
        val dataType = declaration.dataType.type
        val identifier = declaration.identifier

        output.append("${ruleApplier.applySpacesAroundAssignment()}$dataType $identifier;")
        output.append("\n")
    }

    override fun visitAssignDeclare(assignationDeclaration: Node.AssignationDeclaration) {
        val dataType = setDataType(assignationDeclaration.dataType)
        val assignationType = assignationDeclaration.kindVariableDeclaration
        val identifier = assignationDeclaration.identifier
        val value = ValueResolver.resolveValue(assignationDeclaration.value)

        val spacesAroundAssignment = ruleApplier.applySpacesAroundAssignment()
        val newLine = "$assignationType $identifier${ruleApplier.applySpaceForColon()}$dataType$spacesAroundAssignment$value;"
        output.append(newLine)
        output.append("\n")
    }

    override fun visitMethodCall(methodCall: Node.Method) {
        val methodName = methodCall.identifier.value
        val arguments = methodCall.arguments.argumentsOfAnyTypes.joinToString(", ") { arg ->
            ValueResolver.resolveValue(arg)
                .toString()
        }

        output.append("${" ".repeat(rules.newlineBeforePrintln)}$methodName($arguments)")
    }

    private fun setDataType(dataType: Node.DataType): String {
        if (dataType.type == "NUMBER") {
            return "Number"
        }
        return "String"
    }
}
