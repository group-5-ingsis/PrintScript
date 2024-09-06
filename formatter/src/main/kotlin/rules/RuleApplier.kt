
package rules

class RuleApplier(private val rules: FormattingRules) {

    fun applySpacesAroundAssignment(): String {
        val spaceAroundAssignment = rules.spaceAroundAssignment
        return if (spaceAroundAssignment) " = " else "="
    }

    fun applySpaceForColon(): String {
        val spaceBeforeColon = rules.spaceBeforeColon
        val spaceAfterColon = rules.spaceAfterColon

        return when {
            spaceBeforeColon && spaceAfterColon -> " : "
            spaceBeforeColon && !spaceAfterColon -> " :"
            !spaceBeforeColon && spaceAfterColon -> ": "
            else -> ":"
        }
    }
}
