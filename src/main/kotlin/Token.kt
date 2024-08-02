package org.example

data class Token(
    val type: TokenType,
    val value: String,
    val position: Position
)
