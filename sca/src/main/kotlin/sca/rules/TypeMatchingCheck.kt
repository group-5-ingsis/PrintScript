package sca.rules

class TypeMatchingCheck : AnalyzerRules {
    override fun applyRule(configFilePath: String): Boolean {
        return getConfigFileValue(configFilePath, "typeMatchingCheck") { it.toBoolean() }
    }

}
