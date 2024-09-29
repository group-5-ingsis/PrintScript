package parser.semantic.validation

import environment.Environment
import nodes.Statement
import utils.InputProvider

class SemanticValidator(private val inputProvider: InputProvider) {
    private val validatorsForTypes: Map<String, Validator<out Statement>> =
        mapOf(
            "VARIABLE_STATEMENT" to VariableStatementValidator(inputProvider),
            "PRINT" to PrintValidator(),
            "STATEMENT_EXPRESSION" to StatementExpressionValidator(inputProvider)
        )

    fun validateSemantics(node: Statement, varTable: Environment): ValidationResult {
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
