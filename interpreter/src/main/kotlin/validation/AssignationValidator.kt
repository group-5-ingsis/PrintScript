package validation

import composite.Node
import visitor.VariableTable

class AssignationValidator : Validator<Node.Assignation> {
  /* Validate:
   * Assignations on consts.
   * Match assignation type with declared type.
   * Variable exists. */
    override fun validate(node: Node.Assignation): ValidationResult {
        val identifier = node.identifier.value
        val assignedValue = node.value
        return validate(identifier, assignedValue)
    }

    private fun validate(
        identifier: String,
        assignedValue: Node.AssignableValue
    ): ValidationResult {
        if (!exists(identifier)) {
            return ValidationResult(true, null, "Variable with name $identifier was not found.")
        }
        if (isConst(identifier)) {
            return ValidationResult(
                true,
                null,
                "Unable to perform assignation on variable $identifier of kind CONST. "
            )
        }
        if (!variableMatchesAssignedType(identifier, assignedValue)) {
            return ValidationResult(
                true,
                null,
                "Unable to assign value $assignedValue to variable $identifier of type."
            )
        }
        return ValidationResult(false, null, null)
    }

    private fun variableMatchesAssignedType(
        identifier: String,
        assignedValue: Node.AssignableValue
    ): Boolean {
        TODO("Not yet implemented")
    }

    private fun isConst(identifier: String): Boolean {
        VariableTable.getVariable(identifier)
        TODO()
    }

    private fun exists(identifier: String): Boolean {
        return VariableTable.getVariable(identifier) != null
    }
}
