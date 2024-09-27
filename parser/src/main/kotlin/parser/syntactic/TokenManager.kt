package parser.syntactic

import exception.AllowedException
import token.Position
import token.Token
import java.util.NoSuchElementException

class TokenManager private constructor(
    private val tokens: List<Token>,
    private val index: Int
) {

    constructor(tokens: List<Token>) : this(tokens, 0)

    fun advance(): TokenManager {
        if (index >= tokens.size) {
            throw NoSuchElementException("No more tokens to consume.")
        }
        return TokenManager(tokens, index + 1)
    }

    fun consumeType(expectedType: String): TokenManager {
        val token = peek()
        val type = token.type
        if (type != expectedType) {
            throw AllowedException("Expected '$expectedType' at line ${token.position.line}, found '$type'")
        }
        return advance()
    }

    fun consumeValue(expectedValue: String): TokenManager {
        val token = peek()
        val type = token.value
        if (type != expectedValue) {
            throw AllowedException("Expected '$expectedValue' at line ${token.position.line}, found '$type'")
        }
        return advance()
    }

    fun peek(): Token {
        if (index >= tokens.size) {
            throw AllowedException("No tokens to peek at.")
        }
        return tokens[index]
    }

    fun getPosition(): Position {
        return peek().position
    }

    fun isValue(value: String): Boolean {
        if (index >= tokens.size) return false
        val nextTokenType = peek().value
        return nextTokenType == value
    }

    fun nextTokenIsType(type: String): Boolean {
        if (index >= tokens.size) return false
        val nextTokenType = peek().type
        return nextTokenType == type
    }

    fun hasNext(): Boolean {
        return index < tokens.size
    }
}
