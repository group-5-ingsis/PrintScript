package visitor

import composite.Node

object MethodExecute {
    fun executeMethod(
        methodName: String,
        parameters: Node.Arguments
    ): String {
        val methodMap = getRegisteredFunctionsMap()

        val method = methodMap[methodName] ?: throw IllegalArgumentException("Method $methodName is not recognized.")

        val parametersAsString = getParametersAsString(parameters)

        return method(parametersAsString)
    }

    private fun getRegisteredFunctionsMap(): Map<String, (Any?) -> String> {
        val methodMap: Map<String, (Any?) -> String> =
            mapOf(
                "println" to { args ->
                    args.toString()
                }
            )
        return methodMap
    }

    fun getParametersAsString(method: Node.Arguments): String {
        val parameters = method.argumentsOfAnyTypes

        return parameters.joinToString(", ") { argument ->
            when (argument) {
                is Node.Identifier -> {
                    VariableTable.getVariable(argument.value).toString()
                }
                is Node.GenericLiteral -> {
                    argument.value
                }
                is Node.Arguments -> {
                    getParametersAsString(Node.Arguments(argument.argumentsOfAnyTypes))
                }
                else -> throw IllegalArgumentException(
                    "Unsupported argument type: ${argument.nodeType}"
                )
            }
        }
    }
}
