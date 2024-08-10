package interpreter

import parser.NodeResult
import parser.composite.types.Assignation

class NodeVisitor(private val variableTable: VariableTable): Visitor {

    override fun visitAssignation(assignation: Assignation): NodeResult {
        val variableName = assignation.getVariableName()
        val value = assignation.solve()
        variableTable.setVariable(variableName, value)
        return NodeResult(variableName, value)
    }
}