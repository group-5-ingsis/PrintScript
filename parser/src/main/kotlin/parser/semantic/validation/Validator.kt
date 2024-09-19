package parser.semantic.validation

import Environment
import nodes.StatementType

interface Validator<T> {
    fun validate(node: StatementType, scope: Environment): ValidationResult
}
