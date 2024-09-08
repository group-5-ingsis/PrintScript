package position

data class Position(
    val line: Int,
    val symbolIndex: Int
) {
    override fun toString(): String {
        return "Position at line $line, symbol $symbolIndex"
    }
}
