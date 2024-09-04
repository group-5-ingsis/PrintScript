package parser.semantic.validation

import Environment

interface Validator<T> {
    fun validate(node: T, scope: Environment): ValidationResult
}
