package parser.semantic.validation

import position.visitor.Environment

interface Validator<T> {
    fun validate(node: T, scope: Environment): ValidationResult
}
