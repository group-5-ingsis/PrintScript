package composite.types

import composite.Node
import composite.ResultType
import visitor.NodeResult
import visitor.NodeVisitor

class AssignationDeclaration(private val declaration: Declaration, private val assignation: Assignation): Node {

    override fun solve(): NodeResult {
        val declarationResult = declaration.solve()
        val assignationResult = assignation.solve()

        val assignedValue = assignationResult.secondaryValue

        return NodeResult(
            ResultType.ASSIGNATION_DECLARATION,
            declarationResult.primaryValue,
            assignedValue
        )
    }

    override fun accept(visitor: NodeVisitor) {
        visitor.visitAssignDeclare(this)
    }
}