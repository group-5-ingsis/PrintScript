package parser.semantic.validation

import nodes.Statement

// 'where' and 'message' are nullable because the AST could be valid.
data class ValidationResult(val isInvalid: Boolean, val where: Statement?, val message: String?)
