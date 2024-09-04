package parser.semantic.validation

import Environment
import nodes.StatementType
import parser.syntactic.SyntacticParser

class SemanticValidator {
    private val validatorsForTypes: Map<String, Validator<StatementType>> =
        mutableMapOf(
            "VARIABLE_STATEMENT" to VariableStatementValidator() as Validator<StatementType>,
            "PRINT" to PrintValidator() as Validator<StatementType>,
            "STATEMENT_EXPRESSION" to StatementExpressionValidator() as Validator<StatementType>
        )

    fun validateSemantics(nodes: SyntacticParser.RootNode, varTable: Environment): ValidationResult {
        for (node in nodes.getChildren()) {
            val validator = validatorsForTypes[node.statementType]
            validator?.let {
                val result = it.validate(node, varTable)
                if (result.isInvalid) {
                    return result
                }
            }
        }
        return ValidationResult(false, null, null)
    }
}
