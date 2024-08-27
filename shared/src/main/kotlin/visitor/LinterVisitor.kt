package visitor

import Node

class LinterVisitor(private val linterRulesMap: Map<String, Any>) : Visitor {

    override fun visitAssignation(assignation: Node.Assignation) {
        TODO("Not yet implemented")
    }

    override fun visitDeclaration(declaration: Node.Declaration) {
        if (linterRulesMap.containsKey(declaration.identifier)) {
        }
    }

    override fun visitAssignDeclare(assignationDeclaration: Node.AssignationDeclaration) {
        TODO("Not yet implemented")
    }

    override fun visitMethodCall(methodCall: Node.Method) {
        TODO("Not yet implemented")
    }

    override fun getVisitorFunction(nodeType: String): (Node) -> Unit {
        TODO("Not yet implemented")
    }
}
