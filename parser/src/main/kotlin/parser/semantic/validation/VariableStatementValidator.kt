package parser.semantic.validation

import Environment
import nodes.StatementType
import position.visitor.ExpressionVisitor

class VariableStatementValidator : Validator<StatementType.Variable> {

    override fun validate(node: StatementType.Variable, scope: Environment): ValidationResult {
        val assignDeclaration = isAssignDeclaration(node)
        if (assignDeclaration) {
            return validateAssignDeclaration(node, scope)
        }

//        if (scope.get(node.identifier) != null) {
//            return ValidationResult(
//                true,
//                node,
//                "Variable ${node.identifier} has already been declared."
//            )
//        }

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

        val initializerValue = value.acceptVisitor(ExpressionVisitor(), varTable)

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
        return node.initializer != null
    }
}
