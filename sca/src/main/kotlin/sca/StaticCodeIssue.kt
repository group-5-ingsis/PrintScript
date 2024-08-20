package sca

import Position

data class StaticCodeIssue(
    val message: String,
    val position: Position,
)
