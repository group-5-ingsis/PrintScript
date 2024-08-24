package visitor

object MethodExecute {
  private val methodOutput = StringBuilder()

  private fun getRegisteredFunctionsMap(): Map<String, (Any?) -> Unit> {
    val methodMap: Map<String, (Any?) -> Unit> =
      mapOf(
        "println" to { args ->
          val argsToString = args.toString()
          methodOutput.append(argsToString)
        },
      )
    return methodMap
  }

  fun executeMethod(
    methodName: String,
    parameters: String,
  ): String {
    methodOutput.clear()

    val methodMap: Map<String, (Any?) -> Unit> = getRegisteredFunctionsMap()

    val method = methodMap[methodName]

    if (method != null) {
      method(parameters)
    } else {
      throw IllegalArgumentException("Method $methodName is not recognized.")
    }

    val methodOutput = methodOutput.toString()
    return methodOutput
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
