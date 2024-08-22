package parser.validators

import Node

class AssignDeclareValidator : Validator<Node.AssignationDeclaration> {
  // Node structure: AssignationDeclaration(type, kind, identifier, value(value, type))
  // Validation: Type in Declaration MUST match type on assignation.
  override fun validate(node: Node.AssignationDeclaration): ValidationResult {
    val declaredType = node.dataType.type
    val actualType = node.value.nodeType
    return if (declaredType == actualType) {
      ValidationResult(false, null, null)
    } else {
      ValidationResult(
        true,
        null,
        "Declared type $declaredType does not match assigned type $actualType",
      )
    }
  }
}
