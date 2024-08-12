package visitor

import composite.types.Assignation
import composite.types.ResultType


class NodeVisitor(private val variableTable: VariableTable): Visitor {

    override fun visitAssignation(assignation: Assignation): NodeResult {
        val value = assignation.solve()
        variableTable.setVariable("variableName", value)
        return NodeResult(ResultType.ASSIGNATION, value, variableTable)
    }
}