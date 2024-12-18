package parser.semantic.validation

import environment.Environment
import exception.SemanticErrorException
import nodes.Expression
import nodes.StatementType
import visitor.ExpressionVisitor
import visitor.InputProvider

class StatementExpressionValidator(private val inputProvider: InputProvider) : Validator<StatementType.StatementExpression> {

  override fun validate(node: StatementType, scope: Environment): ValidationResult {
    if (node !is StatementType.StatementExpression) {
      return ValidationResult(
        isInvalid = true,
        where = null,
        message = "Invalid statement type '${node::class.simpleName}' for statement expression"
      )
    }

    return when (val exp: Expression = node.value) {
      is Expression.Assign -> {
        handleAssign(exp, scope, node)
      }

      else -> return ValidationResult(false, null, null)
    }
  }

  private fun handleAssign(exp: Expression.Assign, scope: Environment, node: StatementType.StatementExpression): ValidationResult {
    val identifier = exp.name
    val valueExpression = exp.value
    /* null check for getting the value is made on Environment. */
    val variableInfo = scope.get(identifier)
    val actualValue = valueExpression.acceptVisitor(ExpressionVisitor(inputProvider), scope)
    val actualType = getTypeInString(actualValue.first)

    if (variableInfo.designation == "const") {
      throw SemanticErrorException(
        "Variable '$identifier' with modifier 'CONST' does not support assignation. "
      )
    }

    if (!assignedTypeMatchesDeclaredType(actualType, variableInfo.dataType)) {
      throw SemanticErrorException(
        "Declared value '${variableInfo.dataType}' of variable '${variableInfo.identifier}' does not match assigned type '$actualType'."
      )
    }
    return ValidationResult(false, null, null)
  }

  private fun assignedTypeMatchesDeclaredType(actualType: String, dataType: String): Boolean {
    return actualType == dataType
  }

  private fun getTypeInString(actualValue: Any?): String {
    return when (actualValue) {
      is String -> "string"
      is Int -> "number"
      is Float -> "number"
      is Number -> "number"
      is Boolean -> "boolean"
      else -> throw Error("Type not supported")
    }
  }
}
