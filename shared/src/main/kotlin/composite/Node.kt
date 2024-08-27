package composite

import composite.NodeManager.checkIfExist
import visitor.Visitor

sealed class Node {
    abstract val nodeType: String

    abstract fun accept(visitor: Visitor)

    abstract class AssignableValue : Node() {
        abstract fun getType(): DataType
    }

    data class BinaryOperations(val symbol: String, val left: AssignableValue, val right: AssignableValue) : AssignableValue() {
        override val nodeType: String = "BINARY_OPERATION"
        override fun accept(visitor: Visitor) {
            TODO("Not yet implemented")
        }

        override fun getType(): DataType {
            return DataType(nodeType)
        }
    }

    data class Arguments(
        val argumentsOfAnyTypes: List<Node>
    ) : Node() {
        override val nodeType: String = "ARGUMENTS"
        override fun accept(visitor: Visitor) {
            TODO("Not yet implemented")
        }
    }

    data class Identifier(
        val value: String
    ) : AssignableValue() {
        override val nodeType: String = "IDENTIFIER"
        override fun accept(visitor: Visitor) {
            TODO("Not yet implemented")
        }

        override fun getType(): DataType {
            return DataType(nodeType)
        }
    }

    data class Method(
        val arguments: Arguments,
        val identifier: Identifier
    ) : Node() {
        override val nodeType: String = "METHOD_CALL"
        override fun accept(visitor: Visitor) {
            TODO("Not yet implemented")
        }
    }

    data class Declaration(
        val dataType: DataType,
        val kindVariableDeclaration: String,
        val identifier: String
    ) : Node() {
        override val nodeType: String = "DECLARATION"
        override fun accept(visitor: Visitor) {
            return visitor.visitDeclaration(this)
        }
    }

    data class Assignation(
        val identifier: Identifier,
        val value: AssignableValue
    ) : Node() {
        override val nodeType: String = "ASSIGNATION"
        override fun accept(visitor: Visitor) {
            return visitor.visitAssignation(this)
        }
    }

    data class AssignationDeclaration(
        val dataType: DataType,
        val kindVariableDeclaration: String,
        val identifier: String,
        val value: AssignableValue
    ) : Node() {
        override val nodeType: String = "ASSIGNATION_DECLARATION"
        override fun accept(visitor: Visitor) {
            return visitor.visitAssignDeclare(this)
        }
    }

    data class GenericLiteral(
        val value: String,
        val dataType: DataType
    ) : AssignableValue() {
        override val nodeType: String = "LITERAL"
        override fun accept(visitor: Visitor) {
            TODO("Not yet implemented")
        }

        override fun getType(): DataType {
            return dataType
        }
    }

    data class DataType(
        val type: String
    ) : Node() {
        override val nodeType: String = "DATA_TYPE"
        override fun accept(visitor: Visitor) {
            TODO("Not yet implemented")
        }

        init {
            checkIfExist(type)
        }
    }
}
