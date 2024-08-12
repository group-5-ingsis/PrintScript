package visitor

import composite.types.Assignation


class NodeVisitor: Visitor {

    override fun visitAssignation(assignation: Assignation){
        val assignationInfo = assignation.solve()

        val identifier = assignationInfo.primaryValue
        val value = assignationInfo.secondaryValue

        VariableTable.setVariable(identifier.toString(), value)
    }

}