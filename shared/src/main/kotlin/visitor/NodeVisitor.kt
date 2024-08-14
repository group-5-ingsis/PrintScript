package visitor

import composite.types.Assignation
import composite.types.AssignationDeclaration
import composite.types.Declaration


class NodeVisitor: Visitor {

    override fun visitAssignation(assignation: Assignation){
        val assignationInfo = assignation.solve()

        val identifier = assignationInfo.primaryValue
        val value = assignationInfo.secondaryValue

        VariableTable.setVariable(identifier.toString(), value)
    }

    override fun visitDeclaration(declaration: Declaration){
        val declarationInfo = declaration.solve()

        val identifier = declarationInfo.primaryValue

        VariableTable.setVariable(identifier.toString(), "undefined")
    }

    override fun visitAssignDeclare(assignationDeclaration: AssignationDeclaration) {
        val assignDeclarationInfo = assignationDeclaration.solve()

        val identifier = assignDeclarationInfo.primaryValue
        val value = assignDeclarationInfo.secondaryValue

        VariableTable.setVariable(identifier.toString(), value)
    }

}