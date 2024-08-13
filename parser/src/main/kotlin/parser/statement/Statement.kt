package parser.statement

import token.Token

data class Statement(val content: List<Token>, var statementType: String)