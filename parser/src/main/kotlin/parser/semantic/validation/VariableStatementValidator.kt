package parser.semantic.validation

import environment.Environment
import nodes.StatementType
import visitor.ExpressionVisitor

class VariableStatementValidator(private val readInput: String?) : Validator<StatementType.Variable> {

    override fun validate(node: StatementType, scope: Environment): ValidationResult {
        if (node !is StatementType.Variable) {
            return ValidationResult(
                true,
                null,
                "Invalid statement type '${node::class.simpleName}' for variable statement"
            )
        }
        val assignDeclaration = isAssignDeclaration(node)
        if (assignDeclaration) {
            return validateAssignDeclaration(node, scope)
        }

        return validateDeclaration(node)
    }

    private fun validateAssignDeclaration(node: StatementType.Variable, varTable: Environment): ValidationResult {
        val expectedType = node.dataType
        val value = node.initializer
            ?: return ValidationResult(
                true,
                node,
                "Variable '${node.identifier}' has no value assigned."
            )

        val expressionVisitor = ExpressionVisitor(readInput)
        val initializerValue = value.acceptVisitor(expressionVisitor, varTable)

        val actualType = when (initializerValue.first) {
            is String -> "string"
            is Number -> "number"
            is Boolean -> "boolean"
            else -> return ValidationResult(
                true,
                node,
                "Type of initializer for variable '${node.identifier}' is not supported."
            )
        }

        if (actualType != expectedType) {
            return ValidationResult(
                true,
                node,
                "Type mismatch: Expected '$expectedType' but found '$actualType' in variable '${node.identifier}'."
            )
        }

        return ValidationResult(false, null, null)
    }

    private fun validateDeclaration(node: StatementType.Variable): ValidationResult {
        if (node.designation == "const" && node.initializer == null) {
            return ValidationResult(
                true,
                node,
                "Variable '${node.identifier}' is declared as 'const' but has no initializer."
            )
        }

        return ValidationResult(false, null, null)
    }

    private fun isAssignDeclaration(node: StatementType.Variable): Boolean {
        val initializer = node.initializer
        if (initializer?.expressionType == "READ_ENV") {
        }

        return initializer != null
    }
}
