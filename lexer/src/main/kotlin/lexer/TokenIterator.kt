package lexer

import token.Token
import token.TokenGenerator

class TokenIterator(input: String) : Iterator<Token> {
    private val inputIterator = input.iterator()
    private var currentChar: Char? = null
    private var currentBuffer = StringBuilder()
    private var currentRow = 0
    private var currentIndex = 0

    private var nextToken: Token? = null

    override fun hasNext(): Boolean {
        return nextToken != null || inputIterator.hasNext() || currentBuffer.isNotEmpty()
    }

    override fun next(): Token {
        if (nextToken != null) {
            val token = nextToken
            nextToken = null
            return token ?: throw NoSuchElementException("No more tokens")
        }

        while (inputIterator.hasNext()) {
            currentChar = inputIterator.next()
            currentIndex++

            if (currentChar == '\n') {
                currentRow++
                currentIndex = 0
                if (currentBuffer.isNotEmpty()) {
                    return generateTokenFromBuffer()
                }
                continue
            }

            if (currentChar == ';' || currentChar == ':' || currentChar == '(' || currentChar == ')' || currentChar!!.isWhitespace()) {
                if (currentBuffer.isNotEmpty()) {
                    val token = generateTokenFromBuffer()
                    nextToken = if (!currentChar!!.isWhitespace()) {
                        TokenGenerator.generateToken(currentChar.toString(), currentRow, currentIndex - 1)
                    } else {
                        null
                    }
                    return token
                }

                if (!currentChar!!.isWhitespace()) {
                    return TokenGenerator.generateToken(currentChar.toString(), currentRow, currentIndex - 1)
                }
                continue
            }

            currentBuffer.append(currentChar)

            val bufferValue = currentBuffer.toString()
            val currentType = TokenGenerator.getTypeFromValue(bufferValue)

            if (currentType == "KEYWORD" && inputIterator.hasNext()) {
                val nextChar = inputIterator.nextChar()

                if (nextChar.isLetterOrDigit()) {
                    val token = generateTokenFromBuffer()
                    currentBuffer.append(nextChar)
                    return token
                }
            }

            val potentialNextType = TokenGenerator.getTypeFromValue(bufferValue)
            if (currentType != potentialNextType && potentialNextType != "UNKNOWN") {
                currentBuffer.deleteCharAt(currentBuffer.length - 1)
                val token = generateTokenFromBuffer()
                currentBuffer.append(currentChar)
                return token
            }
        }

        if (currentBuffer.isNotEmpty()) {
            return generateTokenFromBuffer()
        }

        throw NoSuchElementException("No more tokens")
    }

    private fun generateTokenFromBuffer(): Token {
        val value = currentBuffer.toString()
        currentBuffer.clear()

        return TokenGenerator.generateToken(value, currentRow, currentIndex - value.length)
    }
}
