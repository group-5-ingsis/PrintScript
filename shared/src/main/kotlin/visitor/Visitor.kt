package visitor

import composite.types.Assignation
import composite.types.Declaration

interface Visitor {
    fun visitAssignation(assignation: Assignation)
    fun visitDeclaration(declaration: Declaration)
}