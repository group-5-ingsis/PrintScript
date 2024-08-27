package parser.validation

import composite.Node

interface Validator<T> {
    fun validate(node: T, varTable: List<Node.Declaration>): ValidationResult
}
