package visitor

import composite.Node
import composite.ResultType
import composite.types.Assignation
import composite.types.AssignationDeclaration
import composite.types.Declaration
import composite.types.MethodCall
import java.sql.Statement


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

    override fun visitMethodCall(methodCall: MethodCall) {
        val methodInfo = methodCall.solve()

        val method = methodInfo.primaryValue as NodeResult
        val methodName = method.primaryValue.toString()

        val parameters : String = getParametersAsString(methodInfo)

        executeMethod(methodName, parameters)
    }

    private fun getParametersAsString(methodInfo: NodeResult): String {

        val type = methodInfo.type

        val isIdentifier = ResultType.IDENTIFIER
        val isLiteral = ResultType.LITERAL
        val isArgument = ResultType.ARGUMENTS

        val methodName = methodInfo.primaryValue

        return when (type) {
            isIdentifier -> {
                VariableTable.getVariable(methodName.toString()).toString()
            }
            isLiteral -> {
                methodName.toString()
            }
            isArgument -> {
                val argumentList = methodName as List<Node>
                getParametersAsString(argumentList[0].solve())
            }
            else -> {
                val arguments = methodInfo.secondaryValue
                getParametersAsString(arguments as NodeResult)
            }
        }
    }

    private fun executeMethod(methodName: String, parameters: String) {

        val methodMap: Map<String, (Any?) -> Unit> = getRegisteredFunctionsMap()

        val method = methodMap[methodName]

        if (method != null) {
            method(parameters)
        } else {
            throw IllegalArgumentException("Method $methodName is not recognized.")
        }
    }

    private fun getRegisteredFunctionsMap(): Map<String, (Any?) -> Unit> {
        val methodMap: Map<String, (Any?) -> Unit> = mapOf(
            "println" to { args ->
                println(args.toString())
            }
        )
        return methodMap
    }


}