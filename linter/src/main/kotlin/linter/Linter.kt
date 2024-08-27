package linter

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import parser.SyntacticParser
import visitor.LinterVisitor
import java.nio.file.Files
import java.nio.file.Paths

class Linter {
    fun lint(rootAstNode: SyntacticParser.RootNode): LinterResult {
        val linterRulesMap = getLinterRules()
        val linterVisitor = LinterVisitor(linterRulesMap)
        try {
            rootAstNode.accept(linterVisitor)
            return LinterResult(true, "No errors found")
        } catch (e: Exception) {
            return LinterResult(false, e.message ?: "Unknown error")
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

    fun getLinterRules(): Map<String, Any> {
        val jsonString = loadJsonFromResources("linter-rules.json")
        return jsonToMap(jsonString)
    }
}
