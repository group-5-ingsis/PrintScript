package parser.syntactic

import exception.InvalidSyntaxException
import token.Position
import token.Token
import java.util.NoSuchElementException

class TokenManager private constructor(
    private val tokens: List<Token>,
    private val index: Int
) {

    constructor(tokens: List<Token>) : this(tokens, 0)

    fun advance(): TokenManager {
        if (index >= tokens.size) throw NoSuchElementException("No more tokens to consume.")
        return TokenManager(tokens, index + 1)
    }

    fun peek(): Token {
        if (index >= tokens.size) throw NoSuchElementException("No tokens to peek at.")
        return tokens[index]
    }

    fun consume(expectedValue: String): TokenManager {
        val token = peek()
        if (token.value != expectedValue) {
            throw InvalidSyntaxException("Expected '$expectedValue' at line ${token.position.line}")
        }
        return advance()
    }

    fun getPosition(): Position {
        return peek().position
    }

    fun isValue(value: String): Boolean {
        if (index >= tokens.size) return false
        return peek().value == value
    }

    fun isType(type: String): Boolean {
        if (index >= tokens.size) return false
        return peek().type == type
    }

    fun hasNext(): Boolean {
        return index < tokens.size
    }

    fun remainingTokens(): List<Token> {
        return tokens.subList(index, tokens.size)
    }
}
