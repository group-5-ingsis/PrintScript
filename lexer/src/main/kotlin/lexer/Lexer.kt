import lexer.LexerState
import token.Token

class Lexer(input: String) : Iterator<Token> {
    private val inputIterator = input.iterator()
    private var lexerState = LexerState()

    private var nextToken: Token? = null

    override fun hasNext(): Boolean {
        return nextToken != null || inputIterator.hasNext() || lexerState.currentBuffer.isNotEmpty()
    }

    override fun next(): Token {
        if (nextToken != null) {
            val token = nextToken
            nextToken = null
            return token ?: throw NoSuchElementException("No more tokens")
        }

        var stateSnapshot = lexerState.copy()

        while (inputIterator.hasNext()) {
            stateSnapshot = stateSnapshot.advance(inputIterator.next())

            if (stateSnapshot.shouldGenerateToken()) {
                val token = stateSnapshot.generateTokenFromBuffer()
                lexerState = stateSnapshot.clearBuffer()
                return token
            }
        }

        if (stateSnapshot.currentBuffer.isNotEmpty()) {
            val token = stateSnapshot.generateTokenFromBuffer()
            lexerState = stateSnapshot.clearBuffer()
            return token
        }

        throw NoSuchElementException("No more tokens")
    }
}
