package token

import Position
import java.io.File

object TokenGenerator {

    fun generateToken(value: String, row : Int, symbolNumber: Int): Token {

        val type = getTypeFromValue(value)
        val position = Position(row + 1, symbolNumber + 1)

        return Token(type, value, position)
    }


    fun getTypeFromValue(value: String): TokenType {

        val typesMap = getTypesMap()

        for ((pattern, type) in typesMap) {

            val patternInMap = Regex(pattern)
            val hasType = value.matches(patternInMap)

            if (hasType) {
                return type
            }

        }
        return TokenType.UNKNOWN
    }


    private fun getTypesMap(): Map<String, TokenType> {
        val assignmentPattern = "="
        val numberPattern = """^-?\d+(\.\d+)?$"""
        val stringPattern = """^".*"$"""
        val punctuationPattern = """^[,;.(){}]$"""
        val variableNamePatter = """^[a-zA-Z_][a-zA-Z0-9_]*$"""

        val variablePattensMap = getVariablePatternsMap()

        val operatorPattern = createPattern(variablePattensMap["OPERATOR"])
        val variableTypePattern = createPattern(variablePattensMap["VARIABLETYPE"])
        val declarationPattern = createPattern(variablePattensMap["DECLARATION"])

        return mapOf(
            assignmentPattern to TokenType.ASSIGNMENT,
            numberPattern to TokenType.NUMBER,
            stringPattern to TokenType.STRING,
            operatorPattern to TokenType.OPERATOR,
            punctuationPattern to TokenType.PUNCTUATION,
            declarationPattern to TokenType.DECLARATION,
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
        // Define the path to the text file
        val filePath = "src/main/resources/token_types.txt"

        // Create a map to store the key-value pairs
        val map = mutableMapOf<String, List<String>>()

        // Read the file line by line
        File(filePath).forEachLine { line ->
            // Split each line into key and value using the delimiter ":"
            val parts = line.split("::")

            // Ensure the line has exactly two parts: key and value
            val key = parts[0].trim() // Convert key to Int
            val values = parts[1].trim().split(",")

            // Add the key-value pair to the map
            map[key] = values
        }

        return map
    }

}