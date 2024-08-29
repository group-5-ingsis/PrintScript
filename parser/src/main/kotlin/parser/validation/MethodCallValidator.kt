package parser.validation

import composite.Node

class MethodCallValidator : Validator<Node.Method> {

  /* Validate:
  * arguments entered match the method arguments' types. */
    override fun validate(node: Node.Method, varTable: List<Node.Declaration>): ValidationResult {
        val arguments = node.arguments
        return validateArguments(node)
    }

    private fun validateArguments(node: Node.Method): ValidationResult {
        val arguments = node.arguments
        for (argument in arguments.argumentsOfAnyTypes) {
        }
        TODO("Not yet implemented")
    }
}
