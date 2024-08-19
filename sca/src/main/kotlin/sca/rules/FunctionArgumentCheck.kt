package sca.rules

class FunctionArgumentCheck : AnalyzerRules {
    override fun applyRule(configFilePath: String): Boolean {
        return getConfigFileValue(configFilePath, "printlnArgumentCheck") { it.toBoolean() }
    }
}
