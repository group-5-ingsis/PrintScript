package visitor

import Node
import composite.NodeType


interface Visitor {
    fun visitAssignation(assignation: Node.Assignation)
    fun visitDeclaration(declaration: Node.Declaration)
    fun visitAssignDeclare(assignationDeclaration: Node.AssignationDeclaration)
    fun visitMethodCall(methodCall: Node.Method)
    fun getVisitorFunction(nodeType: NodeType): (Node) -> Unit
}