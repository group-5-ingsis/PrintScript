package visitor

import composite.Node

class NodeVisitor : Visitor {
    private val output = StringBuilder()

    fun getOutput(): String = output.toString()

    override fun visitAssignation(assignation: Node.Assignation) {
        val identifier = assignation.identifier.value
        val value = ValueResolver.resolveValue(assignation.value)
        VariableTable.setVariable(identifier, value)
    }

    override fun visitDeclaration(declaration: Node.Declaration) {
        val identifier = declaration.identifier
        VariableTable.setVariable(identifier, "undefined")
    }

    override fun visitAssignDeclare(assignationDeclaration: Node.AssignationDeclaration) {
        val identifier = assignationDeclaration.identifier
        val identifierValue = ValueResolver.resolveValue(assignationDeclaration.value)
        VariableTable.setVariable(identifier, identifierValue)
    }

    override fun visitMethodCall(methodCall: Node.Method) {
        val methodName = methodCall.identifier.value
        val params = methodCall.arguments
        val result = MethodExecute.executeMethod(methodName, params)
        output.append(result)
    }
}
