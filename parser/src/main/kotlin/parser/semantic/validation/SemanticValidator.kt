package parser.semantic.validation

import environment.Environment
import nodes.StatementType

class SemanticValidator(private val readInput: String?) {
    private val validatorsForTypes: Map<String, Validator<out StatementType>> =
        mapOf(
            "VARIABLE_STATEMENT" to VariableStatementValidator(readInput),
            "PRINT" to PrintValidator(),
            "STATEMENT_EXPRESSION" to StatementExpressionValidator(readInput)
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
