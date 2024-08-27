package rules

data class FormattingRules(
    val spaceBeforeColon: Boolean,
    val spaceAfterColon: Boolean,
    val spaceAroundAssignment: Boolean,
    val newlineBeforePrintln: Int
)
