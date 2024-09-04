package parser.semantic.validation

import nodes.StatementType

// 'where' and 'message' are nullable because the AST could be valid.
data class ValidationResult(val isInvalid: Boolean, val where: StatementType?, val message: String?)
