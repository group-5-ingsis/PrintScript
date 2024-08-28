package parser.validation

class DeclarationValidator : Validator<Node.Declaration> {
  /* Validate:
   * Don't allow declarations with the same name. */
    override fun validate(node: Node.Declaration, varTable: List<Node.Declaration>): ValidationResult {
        val violations = findViolations(varTable)
        return if (violations.isInvalid) {
            violations
        } else {
            ValidationResult(false, null, null)
        }
    }

    private fun findViolations(tempTable: List<Node.Declaration>): ValidationResult {
        val repeatedElements = tempTable.groupingBy { it.identifier }.eachCount().filter { it.value > 1 }
        return if (repeatedElements.isNotEmpty()) {
            ValidationResult(
                true,
                null,
                "Variable '${repeatedElements.keys.first()}' was declared more than once."
            )
        } else {
            ValidationResult(false, null, null)
        }
    }
}
