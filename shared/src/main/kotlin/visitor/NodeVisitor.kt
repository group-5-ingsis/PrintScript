package visitor

import composite.Node
import composite.ResultType
import composite.types.Assignation
import composite.types.AssignationDeclaration
import composite.types.Declaration
import composite.types.MethodCall


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

        var parameters : String = getStringParam(methodInfo)

        executeMethod(methodName, parameters)
    }

    private fun getStringParam(methodInfo : NodeResult) : String {
      if (methodInfo.type == ResultType.IDENTIFIER) {
        return VariableTable.getVariable(methodInfo.primaryValue.toString()).toString();
      }else if (methodInfo.type == ResultType.LITERAL) {
        return methodInfo.primaryValue.toString();
      }
      else if (methodInfo.type == ResultType.ARGUMENTS) {
        val nodeList : List<Node> = methodInfo.primaryValue as List<Node>
        return getStringParam(nodeList[0].solve())
      }
      return getStringParam(methodInfo.secondaryValue as NodeResult)
    }

    private fun executeMethod(methodName: String, parameters: Any?) {

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