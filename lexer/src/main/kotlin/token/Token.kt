package token

import Position

data class Token(
  val value: String,
  val type: String,
  val position: Position,
)
