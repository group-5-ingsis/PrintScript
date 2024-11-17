package lexer

import token.Token

data class LexerState(
  val buffer: String = "",
  val currentIndex: Int = 0,
  val nextToken: Token? = null
)

fun Char.isQuote(): Boolean = this == '\'' || this == '\"'

fun Char.isSeparator(separators: List<Char>): Boolean = separators.contains(this)
