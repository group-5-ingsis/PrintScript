package parser.syntactic

import exception.InvalidSyntaxException
import token.Position
import token.Token
import java.util.*

class TokenManager(tokens: List<Token>) {

    private val tokenQueue: Queue<Token> = LinkedList(tokens)
    private var lastToken: Token = Token("", "", Position(0, 0))

    fun isValue(value: String): Boolean {
        if (tokenQueue.isEmpty()) {
            return false
        }
        return peek().value == value
    }

    fun isType(value: String): Boolean {
        return tokenQueue.isNotEmpty() && peek().type == value
    }

    fun advance(): Token {
        if (tokenQueue.isEmpty()) {
            return lastToken
        }
        lastToken = tokenQueue.peek()
        return tokenQueue.poll() ?: throw NoSuchElementException("No more tokens to consume.")
    }

    fun peek(): Token {
        if (tokenQueue.isEmpty()) {
            return lastToken
        }
        return tokenQueue.peek() ?: throw NoSuchElementException("No tokens to peek at.")
    }

    fun nextTokenMatchesExpectedType(type: String): Boolean {
        return peek().type == type
    }

    fun getTokens(): List<Token> {
        return tokenQueue.toList()
    }

    fun getPosition(): Position {
        return peek().position
    }

    fun consumeTokenValue(value: String): Token {
        if (peek().value == value) return advance()

        throw InvalidSyntaxException("Expected '$value' after expression in " + peek().position.toString())
    }

    fun consumeTokenType(type: String): Token {
        if (peek().type == type) return advance()

        throw InvalidSyntaxException("Expect this type: $type in " + peek().position.toString())
    }

    fun isNotTheEndOfTokens(): Boolean {
        return (!tokenQueue.isEmpty())
    }

    fun checkTokensAreFromSomeTypes(types: List<String>): Boolean {
        val nextTokenType = peek().type
        return types.contains(nextTokenType)
    }
}
