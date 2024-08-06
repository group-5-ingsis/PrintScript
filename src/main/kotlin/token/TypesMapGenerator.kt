package token

import java.io.File

object TypesMapGenerator {

    fun getTypesMap(): Map<String, TokenType> {

        val assignmentPattern = "="
        val numberPattern = """^-?\d+(\.\d+)?$"""
        val stringPattern = """^".*"$"""
        val punctuationPattern = """^[,;.(){}:]$"""
        val variableNamePatter = """^[a-zA-Z_][a-zA-Z0-9_]*$"""

        val variablePattensMap = getVariablePatternsMap()

        val operatorPattern = createPattern(variablePattensMap["OPERATOR"])
        val variableTypePattern = createPattern(variablePattensMap["VARIABLETYPE"])
        val keywordPattern = createPattern(variablePattensMap["KEYWORD"])

        return mapOf(
            assignmentPattern to TokenType.ASSIGNMENT,
            numberPattern to TokenType.NUMBER,
            stringPattern to TokenType.STRING,
            operatorPattern to TokenType.OPERATOR,
            punctuationPattern to TokenType.PUNCTUATION,
            keywordPattern to TokenType.KEYWORD,
            variableTypePattern to TokenType.VARIABLETYPE,
            variableNamePatter to TokenType.VARIABLENAME
        )

    }

    private fun createPattern(symbols: List<String>?): String {
        var pattern = "^("
        if (symbols != null) {
            for (symbol in symbols) {
                pattern += "$symbol|"
            }
        }
        pattern = pattern.dropLast(1)
        pattern += ")$"
        return pattern
    }

    private fun getVariablePatternsMap(): Map<String, List<String>> {
        val filePath = "src/main/resources/token_types.txt"

        val map = mutableMapOf<String, List<String>>()

        File(filePath).forEachLine { line ->
            val parts = line.split("::")

            val key = parts[0].trim()
            val values = parts[1].trim().split(",")

            map[key] = values
        }

        return map
    }
}