package visitor

import composite.Node2
import composite.NodeType
import composite.types.Assignation
import composite.types.AssignationDeclaration
import composite.types.Declaration
import composite.types.MethodCall

interface Visitor {
    fun visitAssignation(assignation: Assignation)
    fun visitDeclaration(declaration: Declaration)
    fun visitAssignDeclare(assignationDeclaration: AssignationDeclaration)
    fun visitMethodCall(methodCall: MethodCall)
    fun getVisitorFunction(nodeType: NodeType): (Node2) -> Unit
}