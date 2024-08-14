package visitor

import composite.types.Assignation
import composite.types.AssignationDeclaration
import composite.types.Declaration

interface Visitor {
    fun visitAssignation(assignation: Assignation)
    fun visitDeclaration(declaration: Declaration)
    fun visitAssignDeclare(assignationDeclaration: AssignationDeclaration)
}