package parser.validators

import Node
import parser.SyntacticParser

class SemanticValidator {
  private val validatorsForTypes: Map<String, Validator<Node>> =
    mutableMapOf(
      "ASSIGNATION_DECLARATION" to AssignDeclareValidator() as Validator<Node>,
    )

  fun validateSemantics(ast: SyntacticParser.RootNode): ValidationResult {
    for (node: Node in ast) {
      val validator = validatorsForTypes[node.nodeType]
      validator?.let {
        val result = it.validate(node)
        if (result.isInvalid) {
          return result
        }
      }
    }
    return ValidationResult(false, null, null)
  }
}
