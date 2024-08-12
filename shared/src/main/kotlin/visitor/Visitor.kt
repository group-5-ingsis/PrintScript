package visitor

import composite.types.Assignation

interface Visitor {
    fun visitAssignation(assignation: Assignation)
}