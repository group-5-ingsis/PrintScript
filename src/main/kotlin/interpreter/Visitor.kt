package interpreter

import parser.NodeResult
import parser.composite.types.Assignation

interface Visitor {
    fun visitAssignation(assignation: Assignation): NodeResult
}