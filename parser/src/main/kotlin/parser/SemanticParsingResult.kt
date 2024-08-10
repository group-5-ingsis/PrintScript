package parser

import parser.statement.Statement

/* 'where' and 'message' are nullable because the AST could be valid.  */
data class SemanticParsingResult(val isInvalid: Boolean, val where: Statement?, val message: String?)