package parser.statement

import token.Token

data class StatementValResult(val isValid: Boolean, val invalidToken: Token?, val message: String?)
