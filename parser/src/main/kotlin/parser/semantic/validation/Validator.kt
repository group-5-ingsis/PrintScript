package parser.semantic.validation

import environment.Environment
import nodes.StatementType

interface Validator<T> {
  fun validate(node: StatementType, scope: Environment): ValidationResult
}
