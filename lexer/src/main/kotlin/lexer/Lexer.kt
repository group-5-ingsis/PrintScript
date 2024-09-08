package lexer

import token.Token
import token.TokenGenerator

class Lexer(input: String) : Iterator<Token> {
    private val linesIterator = input.lines().iterator()
    private var currentLine: String = ""
    private var lineIterator = currentLine.iterator()
    private var currentRow = 0

    private var state = LexerState()

    override fun hasNext(): Boolean {
        return state.nextToken != null || (linesIterator.hasNext() || lineIterator.hasNext() || state.buffer.isNotEmpty())
    }

    override fun next(): Token {
        if (state.nextToken != null) {
            val token = state.nextToken
            state = state.copy(nextToken = null)
            return token ?: throw NoSuchElementException("No more tokens")
        }

        while (true) {
            if (!lineIterator.hasNext()) {
                if (linesIterator.hasNext()) {
                    currentLine = linesIterator.next()
                    lineIterator = currentLine.iterator()
                    currentRow++
                    state = state.copy(buffer = StringBuilder(), currentIndex = 0)
                    continue
                } else if (state.buffer.isNotEmpty()) {
                    return generateTokenFromBuffer(state.buffer, currentRow, state.currentIndex)
                } else {
                    throw NoSuchElementException("No more tokens")
                }
            }

            val currentChar = lineIterator.next()
            val newIndex = state.currentIndex + 1

            if (currentChar == '\n') {
                currentRow++
                state = state.copy(buffer = StringBuilder(), currentIndex = 0)
                if (state.buffer.isNotEmpty()) {
                    return generateTokenFromBuffer(state.buffer, currentRow, state.currentIndex)
                }
                continue
            }

            val separators = listOf(';', ':', '(', ')', '+', '-', '/', '*')

            if (currentChar == '\'' || currentChar == '\"') {
                val startQuote = currentChar
                val newBuffer = StringBuilder().append(currentChar)
                while (lineIterator.hasNext()) {
                    val nextChar = lineIterator.next()
                    newBuffer.append(nextChar)
                    if (nextChar == startQuote) {
                        break
                    }
                }
                return generateTokenFromBuffer(newBuffer, currentRow, newIndex)
            }

            if (currentChar.isWhitespace() || separators.contains(currentChar)) {
                if (state.buffer.isNotEmpty()) {
                    val token = generateTokenFromBuffer(state.buffer, currentRow, state.currentIndex)
                    state = state.copy(
                        buffer = StringBuilder(), currentIndex = 0,
                        nextToken = if (!currentChar.isWhitespace()) {
                            TokenGenerator.generateToken(currentChar.toString(), currentRow, newIndex - 1)
                        } else {
                            null
                        }
                    )
                    return token
                }

                if (!currentChar.isWhitespace()) {
                    return TokenGenerator.generateToken(currentChar.toString(), currentRow, newIndex - 1)
                }
                continue
            }

            val newBuffer = StringBuilder(state.buffer).append(currentChar)
            val bufferValue = newBuffer.toString()
            val currentType = TokenGenerator.getTypeFromValue(bufferValue)

            val potentialNextType = TokenGenerator.getTypeFromValue(bufferValue)
            if (currentType != potentialNextType && potentialNextType != "UNKNOWN") {
                val trimmedBuffer = StringBuilder(state.buffer).deleteCharAt(state.buffer.length - 1)
                val token = generateTokenFromBuffer(trimmedBuffer, currentRow, state.currentIndex)
                return token
            }

            state = state.copy(buffer = newBuffer, currentIndex = newIndex)
        }
    }

    private fun generateTokenFromBuffer(buffer: StringBuilder, row: Int, index: Int): Token {
        val value = buffer.toString()
        val token = TokenGenerator.generateToken(value, row, index - value.length)
        return token
    }
}
