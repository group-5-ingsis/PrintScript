package lexer

import token.Token

data class LexerState(
  val buffer: String = "",
  val nextToken: Token? = null,
  val currentRow: Int = 0,
  val currentIndex: Int = 0,
  val processedCharacters: Int = 0
)

fun Char.isQuote(): Boolean = this == '\'' || this == '\"'

fun Char.isSeparator(separators: List<Char>): Boolean = separators.contains(this)
