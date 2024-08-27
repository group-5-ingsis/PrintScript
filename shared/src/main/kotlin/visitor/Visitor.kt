package visitor

import composite.Node

interface Visitor {
    fun visitAssignation(assignation: Node.Assignation)

    fun visitDeclaration(declaration: Node.Declaration)

    fun visitAssignDeclare(assignationDeclaration: Node.AssignationDeclaration)

    fun visitMethodCall(methodCall: Node.Method)
}
