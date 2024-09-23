package rules

data class LinterRules(
    private val version: String,
    private val identifierNamingConvention: String = "off",
    private val printlnExpressionAllowed: Boolean = true,
    private val readInputExpressionAllowed: Boolean? = null
) {
    fun getAsMap(): Map<String, Any> {
        val rulesMap = mutableMapOf<String, Any>(
            "identifierNamingConvention" to identifierNamingConvention,
            "printlnExpressionAllowed" to printlnExpressionAllowed
        )

        if (version >= "1.1" && readInputExpressionAllowed != null) {
            rulesMap["readInputExpressionAllowed"] = readInputExpressionAllowed
        }

        return rulesMap
    }
}
