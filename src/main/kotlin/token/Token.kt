package token

import Position

data class Token(
    val type: TokenType,
    val value: String,
    val position: Position
)
