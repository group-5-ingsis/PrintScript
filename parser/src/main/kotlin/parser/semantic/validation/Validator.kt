package parser.semantic.validation

import environment.Environment
import nodes.Statement

interface Validator<T> {
    fun validate(node: Statement, scope: Environment): ValidationResult
}
