package parser.statement

import token.Token

data class StatementValResult(val isInvalid: Boolean, val invalidToken: Token?, val message: String?)
