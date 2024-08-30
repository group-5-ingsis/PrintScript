package parser.validation

import Environment
import nodes.StatementType
import visitor.NodeVisitor

class VariableStatementValidator : Validator<StatementType.Variable> {
    override fun validate(node: StatementType.Variable, scope: Environment): ValidationResult {
        if (scope.get(node.identifier) != null) {
            return ValidationResult(
                true,
                node,
                "Variable " + node.identifier + " has already been declared."
            )
        }

        if (isAssignDeclaration(node)) {
            return validateAssignDeclaration(node, scope)
        }
        return validateDeclaration(node)
    }

    fun validateAssignDeclaration(node: StatementType.Variable, varTable: Environment): ValidationResult {
        val expectedType = node.dataType
        val value = node.initializer
            ?: return ValidationResult(
                true,
                node,
                "Variable '${node.identifier}' has no value assigned."
            )

        val initializerValue = value.acceptVisitor(NodeVisitor(varTable))

        val actualType = when (initializerValue) {
            is String -> "String"
            is Int -> "Int"
            is Float -> "Float"
            is Double -> "Double"
            is Number -> "Number"
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
        return node.initializer != null
    }
}