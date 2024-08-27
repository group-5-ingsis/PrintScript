package parser.validators

import composite.Node

interface Validator<T : Node> {
    fun validate(node: T): ValidationResult
}
