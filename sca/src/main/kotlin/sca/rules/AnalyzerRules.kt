package sca.rules

import org.yaml.snakeyaml.Yaml
import java.io.FileInputStream

interface AnalyzerRules {
    fun applyRule(configFilePath: String): Boolean

    fun getConfigFileValue(
        configFilePath: String,
        ruleName: String,
        convert: (String) -> Boolean
    ): Boolean {
        val input = FileInputStream(configFilePath)
        val yaml = Yaml()
        val data = yaml.load(input) as Map<String, Map<String, Any>>
        val rulesMap = data["rules"] ?: throw IllegalArgumentException("Invalid YAML content")

        val keyValue =
            when (ruleName) {
                "printlnArgumentCheck" -> rulesMap["printlnArgumentCheck"]
                "typeMatchingCheck" -> rulesMap["typeMatchingCheck"].toString()
                "identifierNamingCheck" -> rulesMap["identifierNamingCheck"].toString()
                else -> throw IllegalArgumentException("Invalid rule name")
            }
        return convert(keyValue.toString())
    }
}
