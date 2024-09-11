package rules

class LinterRulesV2(private val identifierNamingConvention: String = "off", private val printlnExpressionAllowed: Boolean = true, private val readInputExpressionAllowed: Boolean = true) : LinterRules {
    override fun getAsMap(): Map<String, Any> {
        return mapOf(
            "identifierNamingConvention" to identifierNamingConvention,
            "printlnExpressionAllowed" to printlnExpressionAllowed,
            "readInputExpressionAllowed" to readInputExpressionAllowed
        )
    }
}
