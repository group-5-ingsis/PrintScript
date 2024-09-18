package parser.semantic.validation

import nodes.Expression
import nodes.StatementType
import position.visitor.Environment

class PrintValidator : Validator<StatementType.Print> {

    override fun validate(node: StatementType, scope: Environment): ValidationResult {
        if (node !is StatementType.Print) {
            return ValidationResult(
                isInvalid = true,
                where = null,
                message = "Invalid statement type '${node::class.simpleName}' for print statement"
            )
        }

        val groupingExpression = node.value
        val innerExpression = groupingExpression.expression

        if (innerExpression is Expression.Binary) {
            val left = innerExpression.left
            val right = innerExpression.right

            val leftValidation = validateExpression(left, scope)
            if (leftValidation.isInvalid) {
                return leftValidation
            }

            val rightValidation = validateExpression(right, scope)
            if (rightValidation.isInvalid) {
                return rightValidation
            }

            return ValidationResult(
                isInvalid = false,
                where = null,
                message = null
            )
        }

        if (innerExpression is Expression.Variable) {
            val name = innerExpression.name
            return try {
                scope.get(name).initializer!!
                ValidationResult(
                    isInvalid = false,
                    where = null,
                    message = null
                )
            } catch (_: Exception) {
                ValidationResult(
                    isInvalid = true,
                    where = node,
                    message = "Variable '$name' does not have an assigned value. "
                )
            }
        }

        // Validate if the expression is a literal (valid to print directly)
        if (innerExpression is Expression.Literal) {
            return ValidationResult(
                isInvalid = false,
                where = null,
                message = null
            )
        }

        // If the expression type is invalid for a print statement, return an error
        return ValidationResult(
            isInvalid = true,
            where = node,
            message = "Invalid expression type '${innerExpression::class.simpleName}' for print statement"
        )
    }

    private fun validateExpression(expression: Expression, scope: Environment): ValidationResult {
        return when (expression) {
            is Expression.Variable -> {
                val name = expression.name
                try {
                    scope.get(name)
                    ValidationResult(
                        isInvalid = false,
                        where = null,
                        message = null
                    )
                } catch (_: Error) {
                    ValidationResult(
                        isInvalid = true,
                        where = null,
                        message = "Variable '$name' is not defined"
                    )
                }
            }
            is Expression.Literal -> ValidationResult(
                isInvalid = false,
                where = null,
                message = null
            )
            is Expression.Binary -> ValidationResult(
                isInvalid = false,
                where = null,
                message = null
            )
            else -> ValidationResult(
                isInvalid = true,
                where = null,
                message = "Invalid expression type '${expression::class.simpleName}' in binary expression"
            )
        }
    }
}
