package runner

import lexer.InputSource
import lexer.Lexer
import parser.Parser

object Runner {

  fun validate(src: InputSource, version: String) {
    try {
      val tokens = Lexer(src, version)
      val asts = Parser(tokens, version)
      while (asts.hasNext()) {
        asts.next()
      }
    } catch (e: Exception) {
      throw e
    }
  }

  fun format() {}

  fun lint() {}

  fun execute() {}
}
