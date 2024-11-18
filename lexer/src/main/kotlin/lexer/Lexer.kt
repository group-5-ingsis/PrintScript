package lexer

import token.Token
import token.TokenGenerator
import java.io.InputStream

class Lexer(inputSource: InputSource, version: String = "1.1") : Iterator<Token> {
  private val lines = inputSource.toBufferedReader().lineSequence().iterator() // Using the BufferedReader from the InputSource
  private var state = LexerState()
  private val tokenGenerator = TokenGenerator(version)
  private val separators = listOf(';', ':', '(', ')', '+', '-', '/', '*', '}', '{', '=')

  companion object {
    fun fromString(input: String, version: String = "1.1"): Lexer {
      val inputSource = StringInputSource(input)
      return Lexer(inputSource, version)
    }

    fun fromInputStream(inputStream: InputStream, version: String = "1.1"): Lexer {
      val inputSource = InputStreamInputSource(inputStream)
      return Lexer(inputSource, version)
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
      val line = readNextLine()
      val (newState, token) = processLine(line)
      state = newState
      if (token != null) return token
    }

    throw NoSuchElementException("No more tokens")
  }

  private fun readNextLine(): String {
    val line = if (lines.hasNext()) lines.next() else ""
    state = state.copy(currentRow = state.currentRow + 1)
    return line
  }

  private fun processLine(line: String): Pair<LexerState, Token?> {
    var buffer = state.buffer
    var currentIndex = state.currentIndex
    val tokens = mutableListOf<Token>()

    for (currentChar in line) {
      currentIndex++
      when {
        currentChar.isWhitespace() -> {
          handleWhitespace(buffer, tokens, currentIndex)
          buffer = ""
        }
        currentChar.isSeparator(separators) -> {
          buffer = handleSeparator(buffer, currentChar, tokens, currentIndex)
        }
        else -> buffer += currentChar
      }
    }

    val nextToken = tokens.firstOrNull()
    return state.copy(
      buffer = buffer,
      currentIndex = currentIndex,
      processedCharacters = state.processedCharacters + line.length,
      nextToken = tokens.getOrNull(1)
    ) to nextToken
  }

  private fun handleWhitespace(
    buffer: String,
    tokens: MutableList<Token>,
    currentIndex: Int
  ) {
    if (buffer.isNotEmpty()) {
      tokens += tokenGenerator.generateToken(buffer, state.currentRow, currentIndex - buffer.length)
    }
  }

  private fun handleSeparator(
    buffer: String,
    currentChar: Char,
    tokens: MutableList<Token>,
    currentIndex: Int
  ): String {
    if (buffer.isNotEmpty()) {
      tokens += tokenGenerator.generateToken(buffer, state.currentRow, currentIndex - buffer.length)
    }
    tokens += tokenGenerator.generateToken(currentChar.toString(), state.currentRow, currentIndex - 1)
    return ""
  }
}
