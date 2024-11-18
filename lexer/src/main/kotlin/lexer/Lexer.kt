package lexer

import token.Token
import token.TokenGenerator
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class Lexer(reader: BufferedReader, version: String = "1.1") : Iterator<Token> {
  private val lines = reader.lineSequence().iterator()
  private var state = LexerState()
  private val tokenGenerator = TokenGenerator(version)
  private val separators = listOf(';', ':', '(', ')', '+', '-', '/', '*', '}', '{', '=')

  companion object {
    fun fromString(input: String, version: String = "1.1"): Lexer {
      return Lexer(BufferedReader(input.reader()), version)
    }

    fun fromInputStream(inputStream: InputStream, version: String = "1.1"): Lexer {
      return Lexer(BufferedReader(InputStreamReader(inputStream)), version)
    }
  }

  override fun hasNext(): Boolean {
    return state.nextToken != null || lines.hasNext() || state.buffer.isNotEmpty()
  }

  override fun next(): Token {
    state.nextToken?.let {
      state = state.copy(nextToken = null)
      return it
    }

    while (lines.hasNext() || state.buffer.isNotEmpty()) {
      val line = lines.takeIf { it.hasNext() }?.next() ?: ""
      state = state.copy(currentRow = state.currentRow + 1)

      val (newState, token) = processLine(line, state)
      state = newState
      if (token != null) return token
    }

    throw NoSuchElementException("No more tokens")
  }

  private fun processLine(line: String, currentState: LexerState): Pair<LexerState, Token?> {
    var buffer = currentState.buffer
    var currentIndex = currentState.currentIndex
    val tokens = mutableListOf<Token>()

    for (currentChar in line) {
      currentIndex++
      when {
        currentChar.isWhitespace() -> {
          if (buffer.isNotEmpty()) {
            tokens += tokenGenerator.generateToken(buffer, currentState.currentRow, currentIndex - buffer.length)
            buffer = ""
          }
        }
        currentChar.isSeparator(separators) -> {
          if (buffer.isNotEmpty()) {
            tokens += tokenGenerator.generateToken(buffer, currentState.currentRow, currentIndex - buffer.length)
            buffer = ""
          }
          tokens += tokenGenerator.generateToken(currentChar.toString(), currentState.currentRow, currentIndex - 1)
        }
        else -> buffer += currentChar
      }
    }

    val nextToken = tokens.firstOrNull()
    return currentState.copy(
      buffer = buffer,
      currentIndex = currentIndex,
      processedCharacters = currentState.processedCharacters + line.length,
      nextToken = tokens.getOrNull(1)
    ) to nextToken
  }
}
