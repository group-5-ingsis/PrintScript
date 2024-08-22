package parser.validators

import visitor.VariableTable

class DeclarationValidator : Validator<Node.Declaration> {
  /* Validate:
   * Don't allow declarations with the same name. */
  override fun validate(node: Node.Declaration): ValidationResult {
    val identifier = node.identifier
    return if (VariableTable.getVariable(identifier) != null) {
      ValidationResult(
        true,
        null,
        "Variable with name $identifier already exists.",
      )
    } else {
      ValidationResult(false, null, null)
    }
  }
}
