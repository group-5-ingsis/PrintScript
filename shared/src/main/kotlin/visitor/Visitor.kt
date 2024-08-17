package visitor

import Node2
import composite.NodeType


interface Visitor {
    fun visitAssignation(assignation: Node2.Assignation)
    fun visitDeclaration(declaration: Node2.Declaration)
    fun visitAssignDeclare(assignationDeclaration: Node2.AssignationDeclaration)
    fun visitMethodCall(methodCall: Node2.Method)
    fun getVisitorFunction(nodeType: NodeType): (Node2) -> Unit
}