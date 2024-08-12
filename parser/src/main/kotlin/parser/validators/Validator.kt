package parser.validators

import parser.SyntacticParser
import token.Token

interface Validator {
  fun validateSyntax(tokenSublist: List<List<Token>>) : ValidationResult
  fun validateSemantics(ast: SyntacticParser.RootNode) : ValidationResult
}