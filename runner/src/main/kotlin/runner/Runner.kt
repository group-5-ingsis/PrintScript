package runner

import lexer.InputSource
import lexer.Lexer

object Runner {

  fun validate(src: InputSource, version: String) {
    val lexer = Lexer(src, version)
  }

  fun format() {}

  fun lint() {}

  fun execute() {}
}
