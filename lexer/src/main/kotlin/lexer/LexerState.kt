package lexer

import token.Token

data class LexerState(
    val buffer: StringBuilder = StringBuilder(),
    val currentIndex: Int = 0,
    val nextToken: Token? = null
)
