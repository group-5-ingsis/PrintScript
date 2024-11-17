package token

data class Token(
  val value: String,
  val type: String,
  val position: Position
)

data class Position(
  val line: Int,
  val symbolIndex: Int
)
