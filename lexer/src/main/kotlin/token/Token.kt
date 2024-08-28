package token

import position.Position

data class Token(
    val value: String,
    val type: String,
    val position: Position
)
