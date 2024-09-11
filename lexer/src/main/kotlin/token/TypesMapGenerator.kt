package token

import java.io.BufferedReader
import java.io.InputStreamReader

object TypesMapGenerator {
    fun getTypesMap(version: String): Map<String, String> {
        return when (version) {
            "1.0" -> getVersionTypesMap("1.0")
            "1.1" -> getVersionTypesMap("1.1")
            else -> throw IllegalArgumentException("Unknown version: $version")
        }
    }

    private fun getVersionTypesMap(version: String): Map<String, String> {
        return getDefaultPatternsMap(version) + getVariablePatternsMap(version)
    }

    private fun getDefaultPatternsMap(version: String): Map<String, String> {
        return when (version) {
            "1.0" -> mapOf(
                "=" to "ASSIGNMENT",
                """^-?\d+(\.\d+)?$""" to "NUMBER",
                """^(['"]).*\1$""" to "STRING",
                """^[,;.(){}:\"\']$""" to "PUNCTUATION"
            )
            "1.1" -> mapOf(
                "=" to "ASSIGNMENT",
                """^-?\d+(\.\d+)?$""" to "NUMBER",
                """^(['"]).*\1$""" to "STRING",
                """^[,;.():\"\']$""" to "PUNCTUATION"
            )
            else -> throw IllegalArgumentException("Unknown version: $version")
        }
    }

    private fun getVariablePatternsMap(version: String): Map<String, String> {
        val fileName = "$version/token_types.txt"
        val map = mutableMapOf<String, String>()

        val inputStream =
            this::class.java.classLoader.getResourceAsStream(fileName)
                ?: throw IllegalArgumentException("Resource not found: $fileName")

        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            reader.forEachLine { line ->
                val parts = line.split("::")
                if (parts.size == 2) {
                    val key = parts[0].trim()
                    val values = parts[1].trim().split(",").map { it.trim() }

                    val pattern = createPattern(values)

                    if (key.isNotEmpty() && pattern.isNotEmpty()) {
                        map[pattern] = key
                    }
                }
            }
        }

        return map
    }

    private fun createPattern(symbols: List<String>): String {
        if (symbols.isEmpty()) return ""
        return "^(${symbols.joinToString("|")})$"
    }
}
