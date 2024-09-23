package parser.semantic.validation

import Environment
import nodes.StatementType
import position.visitor.InputProvider

class SemanticValidator(private val inputProvider: InputProvider) {
    private val validatorsForTypes: Map<String, Validator<out StatementType>> =
        mapOf(
            "VARIABLE_STATEMENT" to VariableStatementValidator(inputProvider),
            "PRINT" to PrintValidator(),
            "STATEMENT_EXPRESSION" to StatementExpressionValidator(inputProvider)
        )

    fun validateSemantics(node: StatementType, varTable: Environment): ValidationResult {
        val validator = validatorsForTypes[node.statementType]
        validator?.let {
            val result = it.validate(node, varTable)
            if (result.isInvalid) {
                return result
            }
        }
        return ValidationResult(false, null, null)
    }
}
