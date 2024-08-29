package parser.validation

import composite.Node
import parser.exceptions.SemanticErrorException

class AssignationValidator : Validator<Node.Assignation> {
  /* Validate:
   * Assignations on consts.
   * Match assignation type with declared type.
   * Variable exists (left and right if variable). */
    override fun validate(node: Node.Assignation, varTable: List<Node.Declaration>): ValidationResult {
        val identifier = node.identifier.value
        val assignedValue = node.value
        return validate(identifier, assignedValue, varTable)
    }

    private fun validate(
        identifier: String,
        assignedValue: Node.AssignableValue,
        varTable: List<Node.Declaration>
    ): ValidationResult {
        if (!exists(identifier, varTable)) {
            return ValidationResult(true, null, "Variable with name '$identifier' was not declared.")
        }
        if (assignedValue is Node.Identifier) {
            if (find(assignedValue.value, varTable) == null) {
                return ValidationResult(true, null, "Variable with name '${assignedValue.value}' was not declared.")
            }
        }
        if (isConst(identifier, varTable)) {
            return ValidationResult(
                true,
                null,
                "Unable to perform assignation on variable '$identifier' of kind CONST. "
            )
        }
        if (!variableMatchesAssignedType(identifier, assignedValue, varTable)) {
            return ValidationResult(
                true,
                null,
                "Unable to assign value $assignedValue to variable '$identifier'."
            )
        }
        return ValidationResult(false, null, null)
    }

    private fun variableMatchesAssignedType(
        identifier: String,
        assignedValue: Node.AssignableValue,
        varTable: List<Node.Declaration>
    ): Boolean {
        val variable = find(identifier, varTable)
        if (variable != null) {
            return variable.dataType == assignedValue.getType()
        } else {
            throw SemanticErrorException("Variable $identifier is not defined.")
        }
    }

    private fun isConst(identifier: String, varTable: List<Node.Declaration>): Boolean {
        val variable = find(identifier, varTable)
        return variable != null && variable.kindVariableDeclaration == "const"
    }

    private fun exists(identifier: String, varTable: List<Node.Declaration>): Boolean {
        return find(identifier, varTable) != null
    }

    private fun find(identifier: String, varTable: List<Node.Declaration>): Node.Declaration? {
        for (variable in varTable) {
            if (variable.identifier == identifier) {
                return variable
            }
        }
        return null
    }
}
