package parser.validation

import Environment

interface Validator<T> {
    fun validate(node: T, scope: Environment): ValidationResult
}
