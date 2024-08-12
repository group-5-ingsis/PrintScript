package visitor

import interpreter.VariableTable
import interpreter.Visitor
import parser.NodeResult
import parser.composite.types.Assignation
import parser.composite.types.ResultType

class NodeVisitor(private val variableTable: VariableTable): Visitor {

    override fun visitAssignation(assignation: Assignation): NodeResult {
        val variableName = assignation.getVariableName()
        val value = assignation.solve()
        variableTable.setVariable(variableName, value)
        return NodeResult(ResultType.ASSIGNATION, value, variableTable)
    }
}