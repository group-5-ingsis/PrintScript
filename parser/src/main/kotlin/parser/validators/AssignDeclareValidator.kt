package parser.validators

import Node

class AssignDeclareValidator {
  // Structure: const a : Number = 33;
  // Node structure: AssignationDeclaration(type, kind, identifier, value(value, type))
  // Validation: Type in Declaration MUST match type on assignation.
  fun validateSemantics(node: Node.AssignationDeclaration): ValidationResult {
    val declaredType = node.nodeType
    val actualType = node.value
    if (declaredType == actualType.nodeType) {
      return ValidationResult(false, null, null)
    } else {
      return ValidationResult(
        true,
        null,
        "Declared type $declaredType does not match assigned type $actualType",
      )
    }
    TODO("Not yet implemented")
  }
}
