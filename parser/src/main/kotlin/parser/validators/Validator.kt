package parser.validators

import Node

interface Validator<T : Node> {
  fun validate(node: T): ValidationResult
}
