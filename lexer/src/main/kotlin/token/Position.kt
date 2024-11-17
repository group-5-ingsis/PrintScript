package token

data class Position(
  val line: Int,
  val symbolIndex: Int
) {
  override fun toString(): String {
    return "Line $line, symbol $symbolIndex"
  }
}
