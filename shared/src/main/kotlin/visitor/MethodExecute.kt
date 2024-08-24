package visitor

object MethodExecute {
  private val output = StringBuilder()

  private fun getRegisteredFunctionsMap(): Map<String, (Any?) -> Unit> {
    val methodMap: Map<String, (Any?) -> Unit> =
      mapOf(
        "println" to { args ->
          output.append(args.toString())
        },
      )
    return methodMap
  }

  fun executeMethod(
    methodName: String,
    parameters: String,
  ): String {
    val methodMap: Map<String, (Any?) -> Unit> = getRegisteredFunctionsMap()

    val method = methodMap[methodName]

    if (method != null) {
      method(parameters)
    } else {
      throw IllegalArgumentException("Method $methodName is not recognized.")
    }

    return output.toString()
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
          "Unsupported argument type: ${argument.nodeType}",
        )
      }
    }
  }
}
