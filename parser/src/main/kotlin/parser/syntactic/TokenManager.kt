package parser.syntactic

import exception.InvalidSyntaxException
import token.Position
import token.Token
import java.util.*

class TokenManager(tokens: List<Token>) {

  private val tokenQueue: Queue<Token> = LinkedList(tokens)
  private var lastToken: Token = Token("", "", Position(0, 0))

  /**
   * See if the current token has x value
   * @return true if the token value is from the current value
   */
  fun isValue(value: String): Boolean {
    if (tokenQueue.isEmpty()) {
      return false
    }
    return peek().value == value
  }

  fun isType(value: String): Boolean {
    return tokenQueue.isNotEmpty() && peek().type == value
  }

  /**
   * Consumes the next token from the queue.
   * @return The token that was consumed.
   * @throws NoSuchElementException if the queue is empty.
   */
  fun advance(): Token {
    if (tokenQueue.isEmpty()) {
      return lastToken
    }
    lastToken = tokenQueue.peek()
    return tokenQueue.poll() ?: throw NoSuchElementException("No more tokens to consume.")
  }

  /**
   * Peeks at the next token in the queue without consuming it.
   * @return The next token in the queue.
   * @throws NoSuchElementException if the queue is empty.
   */
  fun peek(): Token {
    if (tokenQueue.isEmpty()) {
      return lastToken
    }
    return tokenQueue.peek() ?: throw NoSuchElementException("No tokens to peek at.")
  }

  /**
   * Checks if the next token in the queue matches the specified type.
   * @param type The expected token type.
   * @return True if the next token matches the type, false otherwise.
   */
  fun nextTokenMatchesExpectedType(type: String): Boolean {
    return peek().type == type
  }

  /**
   * Returns the remaining tokens in the queue as a list.
   * @return A list of tokens that have not yet been consumed.
   */
  fun getTokens(): List<Token> {
    return tokenQueue.toList()
  }

  /**
   * Returns the position of the next token in the queue.
   * @return The position of the next token.
   * @throws NoSuchElementException if the queue is empty.
   */
  fun getPosition(): Position {
    return peek().position
  }

  /**
   * Consumes the next token in the queue if its value matches the specified value.
   *
   * This function checks if the value of the next token matches the expected value. If it does,
   * the token is consumed (i.e., removed from the queue) and returned. If the value does not match,
   * a `BadSyntacticException` is thrown to indicate that the expected token value was not found.
   *
   * @param value The expected value of the next token.
   * @return The token that was consumed if its value matches the specified value.
   * @throws InvalidSyntaxException if the value of the next token does not match the specified value.
   */
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
