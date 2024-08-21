package parser.validators

import parser.SyntacticParser

interface Validator {
  fun validateSemantics(node: SyntacticParser.RootNode): ValidationResult
}
