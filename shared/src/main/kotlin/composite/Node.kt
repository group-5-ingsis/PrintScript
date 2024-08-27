import composite.NodeManager.checkIfExist
import visitor.Visitor

sealed class Node {
    abstract val nodeType: String

    fun acceptVisitor(visitor: Visitor) {
        val func = visitor.getVisitorFunction(nodeType)
        func(this)
    }

    /**
     * Abstract base class for values that can be assigned to a variable.
     *
     * Represents any value assignable in an assignment statement,
     * such as literals or identifiers.
     */
    abstract class AssignableValue : Node() {
        abstract fun getType(): DataType
    }

    data class BinaryOperations(val symbol: String, val left: AssignableValue, val right: AssignableValue) : AssignableValue() {
        override val nodeType: String = "BINARY_OPERATION"

        override fun getType(): DataType {
            return DataType(nodeType)
        }
    }

    /**
     * Represents a list of arguments in a method call.
     *
     * @param argumentsOfAnyTypes List of arguments.
     */
    data class Arguments(
        val argumentsOfAnyTypes: List<Node>
    ) : Node() {
        override val nodeType: String = "ARGUMENTS"
    }

    /**
     * Represents an identifier in the syntax tree.
     *
     * @param value The name of the identifier.
     */
    data class Identifier(
        val value: String
    ) : AssignableValue() {
        override val nodeType: String = "IDENTIFIER"

        override fun getType(): DataType {
            return DataType(nodeType)
        }
    }

    /**
     * Represents a method call in the syntax tree.
     *
     * @param arguments List of method arguments.
     * @param identifier The method's identifier.
     */
    data class Method(
        val arguments: Arguments,
        val identifier: Identifier
    ) : Node() {
        override val nodeType: String = "METHOD_CALL"
    }

    /**
     * Represents a variable declaration.
     *
     * @param dataType The type of the variable.
     * @param kindVariableDeclaration The kind of declaration.
     * @param identifier The variable's identifier.
     */
    data class Declaration(
        val dataType: DataType,
        val kindVariableDeclaration: String,
        val identifier: String
    ) : Node() {
        override val nodeType: String = "DECLARATION"
    }

    /**
     * Represents an assignment operation.
     *
     * @param identifier The variable being assigned.
     * @param value The value assigned to the variable.
     */
    data class Assignation(
        val identifier: Identifier,
        val value: AssignableValue
    ) : Node() {
        override val nodeType: String = "ASSIGNATION"
    }

    /**
     * Represents a combined assignment and declaration operation.
     *
     * @param dataType The type of the variable.
     * @param kindVariableDeclaration The kind of declaration, e.g., let, const.
     * @param identifier The variable's identifier.
     * @param value The value assigned to the variable.
     */
    data class AssignationDeclaration(
        val dataType: DataType,
        val kindVariableDeclaration: String,
        val identifier: String,
        val value: AssignableValue
    ) : Node() {
        override val nodeType: String = "ASSIGNATION_DECLARATION"
    }

    /**
     * Represents a generic literal value.
     *
     * @param value The literal value.
     * @param dataType The type of the literal value.
     */
    data class GenericLiteral(
        val value: String,
        val dataType: DataType
    ) : AssignableValue() {
        override val nodeType: String = "LITERAL"

        override fun getType(): DataType {
            return dataType
        }
    }

    /**
     * Represents a data type in the syntax tree.
     *
     * @param type The name of the data type.
     */
    data class DataType(
        val type: String
    ) : Node() {
        override val nodeType: String = "DATA_TYPE"

        init {
            checkIfExist(type)
        }
    }
}
