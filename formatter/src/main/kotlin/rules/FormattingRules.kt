package rules

data class FormattingRules(
    val space_before_colon: Boolean,
    val space_after_colon: Boolean,
    val space_around_assignment: Boolean,
    val newline_before_println: Int
)
