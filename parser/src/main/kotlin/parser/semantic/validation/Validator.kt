package parser.semantic.validation

import nodes.StatementType
import position.visitor.Environment

interface Validator<T> {
    fun validate(node: StatementType, scope: Environment): ValidationResult
}
