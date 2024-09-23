package parser.semantic.validation

import Environment
import nodes.Expression
import nodes.StatementType
import position.visitor.ExpressionVisitor
import position.visitor.InputProvider

class VariableStatementValidator(private val inputProvider: InputProvider) : Validator<StatementType.Variable> {

    private fun evaluateExpression(expression: Expression, scope: Environment): Pair<Any?, Environment> {
        val expressionVisitor = ExpressionVisitor(inputProvider)
        return expression.acceptVisitor(expressionVisitor, scope)
    }



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

        if (node.initializer?.expressionType == "READ_INPUT") {
            return validateReadInput(node, node.initializer as Expression.ReadInput)
        }



        val initializerValue = evaluateExpression(value, varTable)

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
        return initializer != null
    }

    private fun validateReadInput(node: StatementType.Variable, readInput: Expression.ReadInput): ValidationResult {
        val valueOfTheReadInput = evaluateExpression(readInput.value, Environment())

        val actualType = convertToCorrespondingType(valueOfTheReadInput.first, node)


        return when {
            node.dataType == "boolean" && actualType is Boolean -> ValidationResult(false, null, null) // Éxito
            node.dataType == "number" && actualType is Number -> ValidationResult(false, null, null) // Éxito
            node.dataType == "string" -> ValidationResult(false, null, null) // Éxito
            else -> ValidationResult(
                true,
                node,
                "Type mismatch: Expected '${node.dataType}' but found '$actualType' in variable '${node.identifier}'."
            )
        }
    }


    private fun convertToCorrespondingType(valueOfTheReadInput: Any?, node: StatementType.Variable): Any? {
        return when (node.dataType) {
            "boolean" -> {
                convertStringToBooleanIfApplicable(valueOfTheReadInput)
            }
            "number" -> {
                convertStringToNumberIfApplicable(valueOfTheReadInput)
            }
            "string" -> {
                valueOfTheReadInput.toString()
            }
            else -> {
                return ValidationResult(
                    true,
                    node,
                    "Type mismatch: Unsupported expected type '${node.dataType}'."
                )
            }
        }
    }

    private fun convertStringToBooleanIfApplicable(value: Any?): Any? {
        return when (value) {
            is String -> when (value) {
                "true" -> true
                "false" -> false
                else -> value // Si no es "true" o "false"
            }
            is Boolean -> value
            else -> value
        }
    }


    private fun convertStringToNumberIfApplicable(value: Any?): Any? {
        return when (value) {
            is String -> value.toDoubleOrNull() ?: value
            is Number -> value
            else -> value
        }
    }
}
