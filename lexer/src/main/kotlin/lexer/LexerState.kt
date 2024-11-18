package lexer

import token.Token

data class LexerState(
  val buffer: String = "",
  val currentIndex: Int = 0,
  val nextToken: Token? = null
)
