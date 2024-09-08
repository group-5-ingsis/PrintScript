package linter

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import parser.syntactic.SyntacticParser
import java.nio.file.Files
import java.nio.file.Paths

class Linter(private val parser: Iterator<SyntacticParser.RootNode>) {

    fun lint(): LinterResult {
        val linterRulesMap = getLinterRules()
        val linterVisitor = LinterVisitor(linterRulesMap)
        val resultBuilder = StringBuilder()
        var noErrors = true

        while (parser.hasNext()) {
            val rootAstNode = parser.next()
            try {
                rootAstNode.accept(linterVisitor)
            } catch (e: Exception) {
                resultBuilder.appendLine(e.message ?: "Unknown error")
                noErrors = false
            }
        }

        return if (noErrors) {
            LinterResult(true, "No errors found")
        } else {
            LinterResult(false, resultBuilder.toString())
        }
    }

    private fun loadJsonFromResources(fileName: String): String {
        val resource = javaClass.getResource("/$fileName")
        if (resource != null) {
            return Files.readString(Paths.get(resource.toURI()))
        }
        throw IllegalArgumentException("Resource not found: $fileName")
    }

    private fun jsonToMap(jsonString: String): Map<String, Any> {
        val jsonElement = Json.parseToJsonElement(jsonString)
        return jsonElement.jsonObject.toMap()
    }

    private fun JsonObject.toMap(): Map<String, Any> = mapValues { entry ->
        when (val jsonElement = entry.value) {
            is JsonObject -> jsonElement.toMap()
            else -> jsonElement.toString()
        }
    }

    private fun getLinterRules(): Map<String, Any> {
        val jsonString = loadJsonFromResources("linter-rules.json")
        return jsonToMap(jsonString)
    }
}
