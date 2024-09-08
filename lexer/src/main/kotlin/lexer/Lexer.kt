package lexer

import token.Token
import token.TokenGenerator

class Lexer(input: String) : Iterator<Token> {
    private val linesIterator = input.lines().iterator()
    private var currentLine: String = ""
    private var lineIterator = currentLine.iterator()
    private var currentRow = 0
    private var currentIndex = 0 // Character index in the current line

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
                    currentIndex = 0 // Reset index for new line
                    state = state.copy(buffer = StringBuilder())
                    continue
                } else if (state.buffer.isNotEmpty()) {
                    return generateTokenFromBuffer(state.buffer)
                } else {
                    throw NoSuchElementException("No more tokens")
                }
            }

            val currentChar = lineIterator.next()
            currentIndex++

            if (currentChar == '\n') {
                currentRow++
                state = state.copy(buffer = state.buffer)
                continue
            }

            val separators = listOf(';', ':', '(', ')', '+', '-', '/', '*')

            if (currentChar == '\'' || currentChar == '\"') {
                val startQuote = currentChar
                val newBuffer = StringBuilder(state.buffer).append(currentChar)
                while (lineIterator.hasNext()) {
                    val nextChar = lineIterator.next()
                    currentIndex++
                    newBuffer.append(nextChar)
                    if (nextChar == startQuote) {
                        break
                    }
                }
                return generateTokenFromBuffer(newBuffer)
            }

            if (currentChar.isWhitespace() || separators.contains(currentChar)) {
                if (state.buffer.isNotEmpty()) {
                    val token = generateTokenFromBuffer(state.buffer)
                    state = state.copy(
                        nextToken = if (!currentChar.isWhitespace()) {
                            TokenGenerator.generateToken(currentChar.toString(), currentRow, currentIndex - 1)
                        } else {
                            null
                        }
                    )
                    return token
                }

                if (!currentChar.isWhitespace()) {
                    return TokenGenerator.generateToken(currentChar.toString(), currentRow, currentIndex - 1)
                }
                continue
            }

            val newBuffer = StringBuilder(state.buffer).append(currentChar)
            state = state.copy(buffer = newBuffer)
        }
    }

    private fun generateTokenFromBuffer(buffer: StringBuilder): Token {
        val value = buffer.toString()
        val symbolIndex = currentIndex - value.length
        val token = TokenGenerator.generateToken(value, currentRow, symbolIndex)
        state = state.copy(buffer = StringBuilder(), currentIndex = symbolIndex)
        return token
    }
}
