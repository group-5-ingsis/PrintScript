package visitor

import composite.types.Assignation
import composite.types.Declaration


class NodeVisitor(private val variableTable: VariableTable): Visitor {

    override fun visitAssignation(assignation: Assignation){
        val value = assignation.solve()
        variableTable.setVariable("variableName", value)
    }

    override fun visitDeclaration(declaration: Declaration){
        val value = declaration.solve()
        variableTable.setVariable("variableName", value)
    }
}