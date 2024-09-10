package rules

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class LinterRulesV1 @JsonCreator constructor(
    @JsonProperty("identifierNamingConvention") val identifierNamingConvention: String = "off",
    @JsonProperty("printlnExpressionAllowed") val printlnExpressionAllowed: Boolean = true
) : LinterRules {
    override fun getAsMap(): Map<String, Any> {
        return mapOf(
            "identifierNamingConvention" to identifierNamingConvention,
            "printlnExpressionAllowed" to printlnExpressionAllowed
        )
    }
}
