package lexer

import token.Position
import token.Token
import token.TokenGenerator
import java.io.InputStream

class Lexer(inputSource: InputSource, version: String = "1.1") : Iterator<Token> {
  private val reader = inputSource.toBufferedReader()
  private val linesIterator = reader.lineSequence().iterator()
  private var currentLine: String = ""
  private var lineIterator = currentLine.iterator()
  private var currentRow = 0
  private var currentIndex = 0
  private val separators = listOf(';', ':', '(', ')', '+', '-', '/', '*', '}', '{', '=')

  private var state = LexerState()
  private val tokenGenerator = TokenGenerator(version)

  private var processedCharacters = 0

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
    return state.nextToken != null || linesIterator.hasNext() || lineIterator.hasNext() || state.buffer.isNotEmpty()
  }

  override fun next(): Token {
    state.nextToken?.let {
      state = state.copy(nextToken = null)
      return it
    }

    while (true) {
      if (!lineIterator.hasNext()) {
        if (!readNextLine()) {
          return if (state.buffer.isNotEmpty()) {
            val position = Position(currentRow, currentIndex - state.buffer.length - 1)
            val token = tokenGenerator.generateToken(state.buffer, position)
            resetBuffer()
            return token
          } else {
            throw NoSuchElementException("No more tokens")
          }
        }
        continue
      }

      val currentChar = lineIterator.next()
      currentIndex++
      processedCharacters++

      when {
        currentChar == '\n' -> handleNewLine()
        currentChar.isQuote() -> {
          val token = handleQuotedLiteral(currentChar)
          return token
        }
        currentChar.isWhitespace() -> {
          val token = handleWhitespace()
          return token
        }
        currentChar.isSeparator(separators) -> {
          val token = handleSeparator(currentChar)
          return token
        }
        else -> accumulateBuffer(currentChar)
      }
    }
  }

  private fun readNextLine(): Boolean {
    return if (linesIterator.hasNext()) {
      currentLine = linesIterator.next()
      lineIterator = currentLine.iterator()
      currentRow++
      currentIndex = 0
      resetBuffer()
      true
    } else {
      false
    }
  }

  private fun handleNewLine() {
    currentRow++
  }

  private fun handleQuotedLiteral(startQuote: Char): Token {
    val newBuffer = StringBuilder(state.buffer).append(startQuote)
    while (lineIterator.hasNext()) {
      val nextChar = lineIterator.next()
      currentIndex++
      newBuffer.append(nextChar)
      if (nextChar == startQuote) {
        break
      }
    }
    val position = Position(currentRow, currentIndex - newBuffer.length - 1)
    val token = tokenGenerator.generateToken(newBuffer.toString(), position)
    resetBuffer()
    return token
  }

  private fun handleWhitespace(): Token {
    if (state.buffer.isNotEmpty()) {
      val position = Position(currentRow, currentIndex - state.buffer.length - 1)
      val token = tokenGenerator.generateToken(state.buffer, position)
      resetBuffer()
      return token
    }
    return next()
  }

  private fun handleSeparator(currentChar: Char): Token {
    if (state.buffer.isNotEmpty()) {
      val position = Position(currentRow, currentIndex - state.buffer.length - 1)
      val token = tokenGenerator.generateToken(state.buffer, position)
      state = state.copy(
        nextToken = handleSeparator(currentChar, currentRow, currentIndex - 1)
      )
      resetBuffer()
      return token
    }

    return handleSeparator(currentChar, currentRow, currentIndex - 1)
  }

  private fun accumulateBuffer(currentChar: Char) {
    state = state.copy(buffer = state.buffer + currentChar)
  }

  private fun resetBuffer() {
    state = state.copy(buffer = "")
  }

  private fun handleSeparator(currentChar: Char, row: Int, index: Int): Token {
    val position = Position(row, index)
    return tokenGenerator.generateToken(currentChar.toString(), position)
  }

  fun getProcessedCharacters(): Int {
    return processedCharacters
  }
}
