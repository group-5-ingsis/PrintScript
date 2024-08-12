package visitor

import composite.types.Assignation
import composite.types.Declaration


class NodeVisitor(private val variableTable: VariableTable): Visitor {

    override fun visitAssignation(assignation: Assignation){
        val assignationInfo = assignation.solve()

        val identifier = assignationInfo.primaryValue
        val value = assignationInfo.secondaryValue

        variableTable.setVariable(identifier.toString(), value)
    }

    override fun visitDeclaration(declaration: Declaration){

        val declarationInfo = declaration.solve()
        val identifier = declarationInfo.primaryValue
        val type = declarationInfo.secondaryValue

        variableTable.setVariable(identifier.toString(), type)
    }
}