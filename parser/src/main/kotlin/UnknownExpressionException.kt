import token.Token

class UnknownExpressionException(token: Token) : Exception("Find unknown expression at line: " + token.position.line + " and at index: " + token.position.symbolIndex) {
}
