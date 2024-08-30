package formatter

import rules.FormattingRules
import rules.RuleApplier
import visitor.Visitor

class FormattingVisitor(private val rules: FormattingRules) : Visitor {
    private val output = StringBuilder()
    private val ruleApplier = RuleApplier(rules)

    fun getFormattedOutput(): String = output.toString()

    override fun visitAssignDeclare(assignationDeclaration: Node.AssignationDeclaration) {
        val assignationType = assignationDeclaration.kindVariableDeclaration
        val identifier = assignationDeclaration.identifier
        val dataType = setDataType(assignationDeclaration.dataType)
        val value = resolveValueWithBinaryOperation(assignationDeclaration.value)

        val spacesAroundAssignment = ruleApplier.applySpacesAroundAssignment()
        val spaceForColon = ruleApplier.applySpaceForColon()
        val newLine = "$assignationType $identifier$spaceForColon$dataType$spacesAroundAssignment$value;"
        output.append(newLine)
        output.append("\n")
    }

    override fun visitAssignation(assignation: Node.Assignation) {
        val identifier = assignation.identifier.value
        val value = resolveValueWithBinaryOperation(assignation.value)

        val spacesAroundAssignment = ruleApplier.applySpacesAroundAssignment()
        val newLine = "$identifier$spacesAroundAssignment$value;"
        output.append(newLine)
        output.append("\n")
    }

    override fun visitDeclaration(declaration: Node.Declaration) {
        val assignationType = declaration.kindVariableDeclaration
        val identifier = declaration.identifier
        val dataType = setDataType(declaration.dataType)

        val applySpaceForColon = ruleApplier.applySpaceForColon()
        val newLine = "$assignationType $identifier$applySpaceForColon$dataType;"
        output.append(newLine)
        output.append("\n")
    }

    override fun visitMethodCall(methodCall: Node.Method) {
        val methodName = methodCall.identifier.value
        val arguments = methodCall.arguments
        val argumentsAsString = MethodExecute.getParametersAsString(arguments)

        if (methodName == "println") {
            val newlinesBeforePrintln = rules.newlineBeforePrintln
            val spaceBeforePrintln = "\n".repeat(newlinesBeforePrintln)
            output.append(spaceBeforePrintln)
        }

        val methodCallLine = "$methodName($argumentsAsString);"
        output.append(methodCallLine)
        output.append("\n")
    }

    private fun setDataType(dataType: Node.DataType): String {
        return when (dataType.type) {
            "NUMBER" -> "Number"
            else -> "String"
        }
    }

    private fun resolveValueWithBinaryOperation(value: Node.AssignableValue): String {
        return if (value is Node.BinaryOperations) {
            val leftValue = ValueResolver.resolveValue(value.left).toString()
            val rightValue = ValueResolver.resolveValue(value.right).toString()
            val symbol = value.symbol
            "$leftValue $symbol $rightValue"
        } else {
            ValueResolver.resolveValue(value).toString()
        }
    }
}
