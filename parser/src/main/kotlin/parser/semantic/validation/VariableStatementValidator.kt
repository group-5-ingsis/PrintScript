package parser.semantic.validation

import environment.Environment
import nodes.Expression
import nodes.StatementType
import visitor.ExpressionVisitor
import visitor.InputProvider

class VariableStatementValidator(private val inputProvider: InputProvider) : Validator<StatementType.Variable> {

  private fun evaluateExpression(expression: Expression, scope: Environment): Pair<Any?, Environment> {
    val expressionVisitor = ExpressionVisitor(inputProvider)
    return expression.acceptVisitor(expressionVisitor, scope)
  }

  override fun validate(node: StatementType, scope: Environment): ValidationResult {
    if (node !is StatementType.Variable) {
      return ValidationResult(
        true,
        null,
        "Invalid statement type '${node::class.simpleName}' for variable statement"
      )
    }
    val assignDeclaration = isAssignDeclaration(node)
    if (assignDeclaration) {
      return validateAssignDeclaration(node, scope)
    }

    return validateDeclaration(node)
  }

  private fun validateAssignDeclaration(node: StatementType.Variable, varTable: Environment): ValidationResult {
    val expectedType = node.dataType
    val value = node.initializer
      ?: return ValidationResult(
        true,
        node,
        "Variable '${node.identifier}' has no value assigned."
      )

    if (node.initializer?.expressionType == "READ_INPUT") {
      return validateReadInput(node, node.initializer as Expression.ReadInput)
    }

    val initializerValue = evaluateExpression(value, varTable)

    val actualType = when (initializerValue.first) {
      is String -> "string"
      is Number -> "number"
      is Boolean -> "boolean"
      else -> return ValidationResult(
        true,
        node,
        "Type of initializer for variable '${node.identifier}' is not supported."
      )
    }

    if (actualType != expectedType) {
      return ValidationResult(
        true,
        node,
        "Type mismatch: Expected '$expectedType' but found '$actualType' in variable '${node.identifier}'."
      )
    }

    return ValidationResult(false, null, null)
  }

  private fun validateReadInput(node: StatementType.Variable, readInput: Expression.ReadInput): ValidationResult {
    val readInput = node.initializer as Expression.ReadInput
    val shouldBeString = readInput.value.expression
    val result = evaluateExpression(shouldBeString, Environment())

    if (result.first is String) {
      return ValidationResult(false, null, null)
    }
    return ValidationResult(
      true,
      node,
      "Expected a string for readInput but got ${result.first} at: " + node.position.toString()
    )
  }

  private fun validateDeclaration(node: StatementType.Variable): ValidationResult {
    if (node.designation == "const" && node.initializer == null) {
      return ValidationResult(
        true,
        node,
        "Variable '${node.identifier}' is declared as 'const' but has no initializer."
      )
    }

    return ValidationResult(false, null, null)
  }

  private fun isAssignDeclaration(node: StatementType.Variable): Boolean {
    val initializer = node.initializer
    return initializer != null
  }
}
