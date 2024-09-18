package lexer

object LexerUtils {
    fun Char.isQuote(): Boolean = this == '\'' || this == '\"'

    fun Char.isSeparator(separators: List<Char>): Boolean = separators.contains(this)
}
