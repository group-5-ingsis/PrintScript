package token

import Position

data class Token(
    val type: String,
    val value: String,
    val position: Position
)
