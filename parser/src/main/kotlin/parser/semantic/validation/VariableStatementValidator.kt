package parser.semantic.validation

import environment.Environment
import nodes.Statement
import visitor.NodeVisitor

class VariableStatementValidator(private val readInput: String?) : Validator<Statement.Variable> {

    override fun validate(node: Statement, scope: Environment): ValidationResult {
        if (node !is Statement.Variable) {
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

    private fun validateAssignDeclaration(node: Statement.Variable, varTable: Environment): ValidationResult {
        val expectedType = node.dataType
        val value = node.initializer
            ?: return ValidationResult(
                true,
                node,
                "Variable '${node.identifier}' has no value assigned."
            )

        val expressionVisitor = NodeVisitor()
        val initializerValue = value.accept(expressionVisitor)

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

    private fun validateDeclaration(node: Statement.Variable): ValidationResult {
        if (node.designation == "const" && node.initializer == null) {
            return ValidationResult(
                true,
                node,
                "Variable '${node.identifier}' is declared as 'const' but has no initializer."
            )
        }

        return ValidationResult(false, null, null)
    }

    private fun isAssignDeclaration(node: Statement.Variable): Boolean {
        val initializer = node.initializer
        if (initializer?.expressionType == "READ_ENV") {
        }

        return initializer != null
    }
}
