package linter

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import lexer.Lexer
import parser.Parser
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

class LinterTests {

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

    @Test
    fun testCamelCaseRule() {
        val linter = Linter()
        val lexer = Lexer
        val parser = Parser()
        val linterRules = getLinterRules()

        val tokens = lexer.lex("let ball_ball : String;")
        val ast = parser.run(tokens)
        val result = linter.lint(ast)
        val namingConvention = linterRules["identifier_naming_convention"]

        if (namingConvention == "\"camel-case\"") {
            println(result.getMessage())
            assertEquals(false, result.isValid())
        } else if (namingConvention == "\"snake-case\"") {
            assertEquals(true, result.isValid())
        } else {
            // Assuming "off" or any other case, the linter should pass
            assertEquals(true, result.isValid())
        }
    }

    @Test
    fun testSnakeCaseRule() {
        val linter = Linter()
        val lexer = Lexer
        val parser = Parser()
        val linterRules = getLinterRules()

        val tokens = lexer.lex("let myString : String;")
        val ast = parser.run(tokens)
        val result = linter.lint(ast)
        val namingConvention = linterRules["identifier_naming_convention"]

        if (namingConvention == "\"snake-case\"") {
            println(result.getMessage())
            assertEquals(false, result.isValid())
        } else if (namingConvention == "\"camel-case\"") {
            assertEquals(true, result.isValid())
        } else {
            // Assuming "off" or any other case, the linter should pass
            assertEquals(true, result.isValid())
        }
    }

    @Test
    fun testOffConvention() {
        val linter = Linter()
        val lexer = Lexer
        val parser = Parser()
        val linterRules = getLinterRules()

        val tokens = lexer.lex("let ball_ball : String;")
        val ast = parser.run(tokens)
        val result = linter.lint(ast)
        val namingConvention = linterRules["identifier_naming_convention"]

        if (namingConvention == "\"off\"") {
            assertEquals(true, result.isValid())
        }
    }

    @Test
    fun testPrintlnCall() {
        val linter = Linter()
        val lexer = Lexer
        val parser = Parser()
        val linterRules = getLinterRules()

        val tokens = lexer.lex("println(2 + 4);")
        val ast = parser.run(tokens)
        val result = linter.lint(ast)
        val expressionAllowed = linterRules["println-expression-allowed"]

        if (expressionAllowed == "false") {
            println(result.getMessage())
            assertEquals(false, result.isValid())
        } else {
            assertEquals(true, result.isValid())
        }
    }
}
