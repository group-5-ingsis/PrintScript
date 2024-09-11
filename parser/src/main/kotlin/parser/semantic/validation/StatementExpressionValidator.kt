package parser.semantic.validation

import Environment
import nodes.Expression
import nodes.StatementType
import position.visitor.ExpressionVisitor

class StatementExpressionValidator : Validator<StatementType.StatementExpression> {

    override fun validate(node: StatementType.StatementExpression, scope: Environment): ValidationResult {
        return when (val exp: Expression = node.value) {
            is Expression.Assign -> {
                handleAssign(exp, scope, node)
            }

//            is Expression.Binary -> TODO()
//            is Expression.Grouping -> TODO()
//            is Expression.IdentifierExpression -> TODO()
//            is Expression.Literal -> TODO()
//            is Expression.Unary -> TODO()
//            is Expression.Variable -> TODO()
            else -> return ValidationResult(false, null, null)
        }
    }

    private fun handleAssign(exp: Expression.Assign, scope: Environment, node: StatementType.StatementExpression): ValidationResult {
        val identifier = exp.name
        val valueExpression = exp.value
        /* null check for getting the value is made on Environment. */
        val variableInfo = scope.get(identifier)
        val actualValue = valueExpression.acceptVisitor(ExpressionVisitor(), scope)
        val actualType = getTypeInString(actualValue)

        if (getTypeInString(variableInfo) != actualType) {
            return ValidationResult(
                true,
                node,
                "Type mismatch: Expected '$variableInfo' but found '$actualType' in assignment to '$identifier'."
            )
        }
        return ValidationResult(false, null, null)
    }

    private fun getTypeInString(actualValue: Any?): String {
        return when (actualValue) {
            is String -> "string"
//            is Int -> "Int"
//            is Float -> "Float"
//            is Double -> "Double"
            is Number -> "number"
            is Boolean -> "boolean"
            else -> throw Error("Type not supported")
        }
    }
}
