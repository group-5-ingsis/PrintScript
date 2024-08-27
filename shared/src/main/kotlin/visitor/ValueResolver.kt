package visitor

import composite.Node

object ValueResolver {

    fun resolveValue(valueNode: Node): Any? {
        return when (valueNode) {
            is Node.GenericLiteral -> resolveLiteralValue(valueNode)
            is Node.Identifier -> resolveIdentifierValue(valueNode)
            is Node.BinaryOperations -> BinaryEvaluator.evaluate(valueNode)
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
}
