package visitor

import Node

class NodeVisitor : Visitor {
  override fun visitAssignation(assignation: Node.Assignation) {
    val identifier = assignation.identifier.value
    val value = resolveValue(assignation.value)
    VariableTable.setVariable(identifier, value)
  }

  override fun visitDeclaration(declaration: Node.Declaration) {
    val identifier = declaration.identifier
    VariableTable.setVariable(identifier, "undefined")
  }

  override fun visitAssignDeclare(assignationDeclaration: Node.AssignationDeclaration) {
    val identifier = assignationDeclaration.identifier
    val identifierValue = resolveAssignableValue(assignationDeclaration.value)
    VariableTable.setVariable(identifier, identifierValue)
  }

  private fun resolveAssignableValue(value: Node.AssignableValue): Any? {
    return when (value) {
      is Node.GenericLiteral -> resolveLiteralValue(value)
      is Node.Identifier -> resolveIdentifierValue(value)
      is Node.BinaryOperations -> BinaryEvaluator.evaluate(value)
      else -> throw Exception("Implement other cases of AssignationValue Types")
    }
  }

  private fun resolveValue(valueNode: Node): Any? {
    return when (valueNode) {
      is Node.GenericLiteral -> resolveLiteralValue(valueNode)
      is Node.Identifier -> resolveIdentifierValue(valueNode)
      else -> throw IllegalArgumentException(
        "Unsupported AssignationValue type: ${valueNode.nodeType}",
      )
    }
  }

  private fun resolveLiteralValue(literal: Node.GenericLiteral): Any? {
    return when (literal.dataType.type) {
      "NUMBER" -> literal.value.toIntOrNull()
      "INT" -> literal.value.toIntOrNull() ?: throw IllegalArgumentException("El valor no es un entero válido")
      "FLOAT" -> literal.value.toFloatOrNull() ?: throw IllegalArgumentException("El valor no es un flotante válido")
      "LONG" -> literal.value.toLongOrNull() ?: throw IllegalArgumentException("El valor no es un entero largo válido")
      "SHORT" -> literal.value.toShortOrNull() ?: throw IllegalArgumentException("El valor no es un entero corto válido")
      "STRING" -> literal.value
      else -> throw IllegalArgumentException("Tipo de dato no soportado: ${literal.dataType.type}")
    }
  }

  private fun resolveIdentifierValue(identifier: Node.Identifier): Any? {
    return VariableTable.getVariable(identifier.value)
      ?: throw RuntimeException("Variable ${identifier.value} is not defined")
  }

  override fun visitMethodCall(methodCall: Node.Method) {
    val methodName = methodCall.identifier.value

    // Convierte los argumentos en una cadena de texto
    val parameters = getParametersAsString(methodCall.arguments)

    // Ejecuta el método con los parámetros
    executeMethod(methodName, parameters)
  }

  override fun getVisitorFunction(nodeType: String): (Node) -> Unit {
    return when (nodeType) {
      "ASSIGNATION" -> { node ->
        visitAssignation(node as Node.Assignation)
      }

      "DECLARATION" -> { node ->
        visitDeclaration(node as Node.Declaration)
      }

      "ASSIGNATION_DECLARATION" -> { node ->
        visitAssignDeclare(node as Node.AssignationDeclaration)
      }

      "METHOD_CALL" -> { node ->
        visitMethodCall(node as Node.Method)
      }

      "LITERAL" -> {
        TODO("Not implemented yet")
      }

      "IDENTIFIER" -> {
        TODO("Not implemented yet")
      }

      "DATA_TYPE" -> {
        TODO("Not implemented yet")
      }

      "ARGUMENTS" -> {
        TODO("Not implemented yet")
      }

      "METHOD_NAME" -> {
        TODO("Not implemented yet")
      }

      else -> {
        TODO("Not implemented yet")
      }
    }
  }

  private fun getParametersAsString(method: Node.Arguments): String {
    val parameters = method.argumentsOfAnyTypes

    return parameters.joinToString(", ") { argument ->
      when (argument) {
        is Node.Identifier -> {
          // Recupera el valor de la tabla de variables usando el identificador
          VariableTable.getVariable(argument.value).toString()
        }

        is Node.GenericLiteral -> {
          // Devuelve directamente el valor del literal
          argument.value
        }

        is Node.Arguments -> {
          // Si es un nodo de argumentos, procesa los argumentos recursivamente
          getParametersAsString(Node.Arguments(argument.argumentsOfAnyTypes))
        }

        else -> throw IllegalArgumentException(
          "Tipo de argumento no soportado: ${argument.nodeType}",
        )
      }
    }
  }

  private fun executeMethod(
    methodName: String,
    parameters: String,
  ) {
    val methodMap: Map<String, (Any?) -> Unit> = getRegisteredFunctionsMap()

    val method = methodMap[methodName]

    if (method != null) {
      method(parameters)
    } else {
      throw IllegalArgumentException("Method $methodName is not recognized.")
    }
  }

  private fun getRegisteredFunctionsMap(): Map<String, (Any?) -> Unit> {
    val methodMap: Map<String, (Any?) -> Unit> =
      mapOf(
        "println" to { args ->
          println(args.toString())
        },
      )
    return methodMap
  }
}
