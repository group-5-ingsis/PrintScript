package parser.validation

import composite.Node

class SemanticValidator {
    private val validatorsForTypes: Map<String, Validator<Node>> =
        mutableMapOf(
            "ASSIGNATION_DECLARATION" to AssignDeclareValidator() as Validator<Node>,
            "DECLARATION" to DeclarationValidator() as Validator<Node>,
            "ASSIGNATION" to AssignationValidator() as Validator<Node>
        )

    fun validateSemantics(nodes: List<Node>, varTable: List<Node.Declaration>): ValidationResult {
        for (node in nodes) {
            val validator = validatorsForTypes[node.nodeType]
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
