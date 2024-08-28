package parser.statement

import position.Position
import token.Token

object SyntacticErrorHandler {
    fun handle(validationResult: StatementValResult) {
        if (!validationResult.isValid) {
            val invalidToken: Token = validationResult.invalidToken ?: return
            val errorPosition: Position = invalidToken.position
            val errorMessage = "Error at line ${errorPosition.line}, column ${errorPosition.symbolIndex}: ${validationResult.message}"
            println(errorMessage)
            throw IllegalArgumentException(errorMessage)
        }
    }
}
