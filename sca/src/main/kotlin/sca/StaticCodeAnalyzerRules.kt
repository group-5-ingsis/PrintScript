package sca

import sca.rules.FunctionArgumentCheck
import sca.rules.IdentifierNamingCheck
import sca.rules.TypeMatchingCheck

data class StaticCodeAnalyzerRules(
    val configFilePath: String,
    val functionArgumentCheck: Boolean = FunctionArgumentCheck().applyRule(configFilePath),
    val typeMatchingCheck: Boolean = TypeMatchingCheck().applyRule(configFilePath),
    val identifierNamingCheck: Boolean = IdentifierNamingCheck().applyRule(configFilePath),
)
