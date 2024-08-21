package parser.validators

import Node
import parser.SyntacticParser

class SemanticValidator : Validator {
  val validatorsForTypes =
    mutableMapOf(
      "ASSIGNATION_DECLARATION" to AssignDeclareValidator(),
    )

  override fun validateSemantics(ast: SyntacticParser.RootNode): ValidationResult {
    for (node: Node in ast) {
      val validator = validatorsForTypes[node.nodeType]
    }
    return ValidationResult(false, null, null)
  }
}
