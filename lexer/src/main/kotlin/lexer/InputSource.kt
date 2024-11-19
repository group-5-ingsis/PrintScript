package lexer

import java.io.BufferedReader
import java.io.InputStream

sealed interface InputSource {
  fun toBufferedReader(): BufferedReader
}

class StringInputSource(private val value: String) : InputSource {
  override fun toBufferedReader(): BufferedReader {
    return value.reader().buffered()
  }
}

class InputStreamInputSource(private val stream: InputStream) : InputSource {
  override fun toBufferedReader(): BufferedReader {
    return stream.bufferedReader()
  }
}

fun Char.isQuote(): Boolean = this == '\'' || this == '\"'

fun Char.isSeparator(separators: List<Char>): Boolean = separators.contains(this)
