package sca.rules

class IdentifierNamingCheck : AnalyzerRules {
    override fun applyRule(configFilePath: String): Boolean {
        return getConfigFileValue(configFilePath, "identifierNamingCheck") { it.toBoolean() }
    }
}
