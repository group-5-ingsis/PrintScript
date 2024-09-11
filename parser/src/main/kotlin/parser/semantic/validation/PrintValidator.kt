package parser.semantic.validation

import nodes.Expression
import nodes.StatementType
import position.visitor.Environment

class PrintValidator : Validator<StatementType.Print> {

    override fun validate(node: StatementType.Print, scope: Environment): ValidationResult {
        val groupingExpression = node.value
        val innerExpression = groupingExpression.expression

        if (innerExpression is Expression.Variable) {
            try {
                val name = innerExpression.name
                val value = scope.get(name)
                return ValidationResult(
                    isInvalid = false,
                    where = null,
                    message = null
                )
            } catch (_: Error) {
                return ValidationResult(
                    isInvalid = true,
                    where = node,
                    message = "Invalid expression type '${innerExpression::class.simpleName}' for print statement"
                )
            }
        }

        if (innerExpression is Expression.Literal) {
            return ValidationResult(
                isInvalid = false,
                where = null,
                message = null
            )
        }

        return ValidationResult(
            isInvalid = true,
            where = node,
            message = "Invalid expression type '${innerExpression::class.simpleName}' for print statement"
        )
    }
}
