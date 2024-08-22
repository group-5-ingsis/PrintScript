package visitor

import Node

class NodeVisitor : Visitor {
  override fun visitAssignation(assignation: Node.Assignation) {
    val identifier = assignation.identifier.value
    val value =
      when (val valueNode = assignation.value) {
        is Node.GenericLiteral -> {
          when (valueNode.dataType.type) {
            "NUMBER" -> {
              // Convierte el valor a Double, manejando varios tipos numéricos
              stringToNumber(valueNode.value)
            }
            "INT" -> {
              // Convierte a Int si es un entero
              valueNode.value.toIntOrNull() ?: throw IllegalArgumentException("El valor no es un entero válido")
            }
            "FLOAT" -> {
              // Convierte a Float si es un número con decimales más pequeño
              valueNode.value.toFloatOrNull() ?: throw IllegalArgumentException("El valor no es un flotante válido")
            }
            "LONG" -> {
              // Convierte a Long si es un número entero long
              valueNode.value.toLongOrNull() ?: throw IllegalArgumentException("El valor no es un entero largo válido")
            }
            "SHORT" -> {
              // Convierte a Short si es un número entero corto
              valueNode.value.toShortOrNull() ?: throw IllegalArgumentException("El valor no es un entero corto válido")
            }
            "STRING" -> {
              valueNode.value
            }
            else -> throw IllegalArgumentException(
              "Tipo de dato no soportado: ${valueNode.dataType.type}",
            )
          }
        }
        is Node.Identifier -> {
          VariableTable.getVariable(valueNode.value)
            ?: throw RuntimeException("Variable ${valueNode.value} is not defined")
        }

        else -> throw IllegalArgumentException(
          "Unsupported AssignationValue type: ${valueNode.nodeType}",
        )
      }
    // Guardarlo
    VariableTable.setVariable(identifier, value)
  }

  private fun stringToNumber(value: String): Number {
    return when {
      value.contains('.') -> {
        // Si el valor contiene un punto decimal, tratamos de convertirlo a Double
        value.toDoubleOrNull() ?: throw IllegalArgumentException("El valor no es un número válido")
      }
      else -> {
        // Si el valor no contiene un punto decimal, tratamos de convertirlo a Int
        value.toIntOrNull() ?: throw IllegalArgumentException("El valor no es un número válido")
      }
    }
  }

  override fun visitDeclaration(declaration: Node.Declaration) {
    // Extrae el identificador de la declaración

    val identifier = declaration.identifier
    VariableTable.setVariable(identifier, "undefined")
  }

  override fun visitAssignDeclare(assignationDeclaration: Node.AssignationDeclaration) {
    val identifier = assignationDeclaration.identifier
    val identifierValue =

      when (val value: Node.AssignationValue = assignationDeclaration.value) {
        is Node.GenericLiteral -> {
          if (value.dataType.type != "STRING") {
            stringToNumber(value.value)
          } else {
            value.value
          }
        }
        is Node.Identifier -> {
          VariableTable.getVariable(value.value)
        }

        is Node.BinaryOperations -> {
          evaluateBinaryOperation(value)
        }

        else -> {
          throw Exception("implement other cases of AssignationValue Types")
        }
      }

    VariableTable.setVariable(identifier, identifierValue)
  }

  private fun evaluateBinaryOperation(binaryOp: Node.BinaryOperations): Any {
    val leftValue =
      when (val left = binaryOp.left) {
        is Node.GenericLiteral ->
          if (left.dataType.type != "STRING") {
            stringToNumber(left.value)
          } else {
            left.value
          }
        is Node.Identifier -> VariableTable.getVariable(left.value)
        is Node.BinaryOperations -> evaluateBinaryOperation(left)
        else -> throw Exception("Unsupported type in binary operation left side")
      }

    val rightValue =
      when (val right = binaryOp.right) {
        is Node.GenericLiteral ->
          if (right.dataType.type != "STRING") {
            stringToNumber(right.value)
          } else {
            right.value
          }

        is Node.Identifier -> VariableTable.getVariable(right.value)
        is Node.BinaryOperations -> evaluateBinaryOperation(right)
        else -> throw Exception("Unsupported type in binary operation right side")
      }

    val leftIsNumber = leftValue is Number
    val rightIsNumber = rightValue is Number

    return when (binaryOp.symbol) {
      "+" -> {
        when {
          leftValue is String && rightValue is String -> leftValue + rightValue
          leftIsNumber && rightIsNumber -> (leftValue as Number).toDouble() + (rightValue as Number).toDouble()
          leftValue is String || rightValue is String -> leftValue.toString() + rightValue.toString()
          else -> throw Exception("Unsupported operands for addition")
        }
      }
      "-" -> {
        if (leftIsNumber && rightIsNumber) {
          (leftValue as Number).toDouble() - (rightValue as Number).toDouble()
        } else {
          throw Exception("Subtraction requires both operands to be numbers")
        }
      }
      "*" -> {
        if (leftIsNumber && rightIsNumber) {
          (leftValue as Number).toDouble() * (rightValue as Number).toDouble()
        } else {
          throw Exception("Multiplication requires both operands to be numbers")
        }
      }
      "/" -> {
        if (leftIsNumber && rightIsNumber) {
          (leftValue as Number).toDouble() / (rightValue as Number).toDouble()
        } else {
          throw Exception("Division requires both operands to be numbers")
        }
      }
      else -> throw Exception("Unsupported binary operator")
    }
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
