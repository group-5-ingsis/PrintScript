package parser.semantic.validation

import nodes.Expression
import nodes.StatementType
import position.visitor.Environment

class PrintValidator : Validator<StatementType.Print> {

    override fun validate(node: StatementType.Print, scope: Environment): ValidationResult {
        val groupingExpression = node.value
        val innerExpression = groupingExpression.expression

        // Validate if the innerExpression is a variable
        if (innerExpression is Expression.Variable) {
            val name = innerExpression.name
            return try {
                val variable = scope.get(name) // This will throw an error if the variable is not found
                ValidationResult(
                    isInvalid = false,
                    where = null,
                    message = null
                )
            } catch (e: Error) {
                // If an error is caught, it means the variable was not found
                ValidationResult(
                    isInvalid = true,
                    where = node,
                    message = "Variable '$name' is not defined"
                )
            }
        }

        // Validate if the innerExpression is a literal
        if (innerExpression is Expression.Literal) {
            return ValidationResult(
                isInvalid = false,
                where = null,
                message = null
            )
        }

        // If it's neither a variable nor a literal, return an invalid result
        return ValidationResult(
            isInvalid = true,
            where = node,
            message = "Invalid expression type '${innerExpression::class.simpleName}' for print statement"
        )
    }
}
