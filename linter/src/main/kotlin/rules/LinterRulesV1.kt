package rules

data class LinterRulesV1(private val identifierNamingConvention: String = "off", val printlnExpressionAllowed: Boolean = true) : LinterRules {
    override fun getAsMap(): Map<String, Any> {
        return mapOf(
            "identifierNamingConvention" to identifierNamingConvention,
            "printlnExpressionAllowed" to printlnExpressionAllowed
        )
    }
}
