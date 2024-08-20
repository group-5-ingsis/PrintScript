package visitor

import Node
import composite.NodeType

class NodeVisitor : Visitor {
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

    override fun visitAssignation(assignation: Node.Assignation) {
        // Extrae el identificador y el valor del nodo de asignación

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

    override fun visitDeclaration(declaration: Node.Declaration) {
        // Extrae el identificador de la declaración

        val identifier = declaration.identifier
        VariableTable.setVariable(identifier, "undefined")
    }

    override fun visitAssignDeclare(assignationDeclaration: Node.AssignationDeclaration) {
        // Extrae la información de la asignación y declaración
        val identifier = assignationDeclaration.identifier
        val value: Node.AssignationValue = assignationDeclaration.value
        val identifierValue =
            when (value) {
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

                else -> {
                    throw Exception("implement other cases of AssignationValue Types")
                }
            }

        // Establece la variable en la tabla de variables con el valor proporcionado
        VariableTable.setVariable(identifier, identifierValue)
    }

    override fun visitMethodCall(methodCall: Node.Method) {
        val methodName = methodCall.identifier.value

        // Convierte los argumentos en una cadena de texto
        val parameters = getParametersAsString(methodCall.arguments)

        // Ejecuta el método con los parámetros
        executeMethod(methodName, parameters)
    }

    override fun getVisitorFunction(nodeType: NodeType): (Node) -> Unit {
        return when (nodeType) {
            NodeType.ASSIGNATION -> { node ->
                visitAssignation(node as Node.Assignation)
            }

            NodeType.DECLARATION -> { node ->
                visitDeclaration(node as Node.Declaration)
            }

            NodeType.ASSIGNATION_DECLARATION -> { node ->
                visitAssignDeclare(node as Node.AssignationDeclaration)
            }

            NodeType.METHOD_CALL -> { node ->
                visitMethodCall(node as Node.Method)
            }

            NodeType.LITERAL -> {
                TODO("Not implemented yet")
            }

            NodeType.IDENTIFIER -> {
                TODO("Not implemented yet")
            }

            NodeType.DATA_TYPE -> {
                TODO("Not implemented yet")
            }

            NodeType.ARGUMENTS -> {
                TODO("Not implemented yet")
            }

            NodeType.METHOD_NAME -> {
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
