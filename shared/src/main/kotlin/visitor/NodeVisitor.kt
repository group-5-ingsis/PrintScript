package visitor

import composite.types.Assignation


class NodeVisitor(private val variableTable: VariableTable): Visitor {

    override fun visitAssignation(assignation: Assignation){
        val assignationInfo = assignation.solve()

        val identifier = assignationInfo.primaryValue
        val value = assignationInfo.secondaryValue

        variableTable.setVariable(identifier.toString(), value)
    }

}