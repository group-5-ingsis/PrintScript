package parser.semantic.validation

import nodes.StatementType
import position.visitor.Environment

class PrintValidator : Validator<StatementType.Print> {

    override fun validate(node: StatementType.Print, scope: Environment): ValidationResult {
        val groupingExpression = node.value

        val innerExpression = groupingExpression.expression

        val expressionType = scope.getTypeForValue(innerExpression)

        if (expressionType !in listOf("STRING", "NUMBER", "BOOLEAN")) {
            return ValidationResult(
                isInvalid = true,
                where = node,
                message = "Invalid type '$expressionType' for print statement"
            )
        }

        return ValidationResult(
            isInvalid = false,
            where = null,
            message = null
        )
    }
}
