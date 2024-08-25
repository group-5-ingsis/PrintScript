package visitor

import Node

class NodeVisitor : Visitor {
    private val output = StringBuilder()

    fun getOutput(): String = output.toString()

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
                "Unsupported AssignationValue type: ${valueNode.nodeType}"
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

    private fun resolveIdentifierValue(identifier: Node.Identifier): Any {
        return VariableTable.getVariable(identifier.value)
            ?: throw RuntimeException("Variable ${identifier.value} is not defined")
    }

    override fun visitMethodCall(methodCall: Node.Method) {
        val methodName = methodCall.identifier.value
        val params = methodCall.arguments
        val result = MethodExecute.executeMethod(methodName, params)
        output.append(result)
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

            else -> {
                TODO("Not implemented yet")
            }
        }
    }
}
