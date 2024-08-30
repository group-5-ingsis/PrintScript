package nodes

object DataTypeManager {
    private val listOfAllowDataTypes = listOf(
        "String",
        "Number",
        "Int",
        "Float",
        "Double"
    )
    private val checkOfAllowedKeyWordsForDeclareVariables = listOf(
        "let",
        "const"
    )

    fun checkDataType(dataType: String) {
        if (!listOfAllowDataTypes.contains(dataType)) {
            throw IllegalArgumentException("$dataType is not allowed")
        }
    }

    fun checkVariableDec(variableDec: String) {
        if (!checkOfAllowedKeyWordsForDeclareVariables.contains(variableDec)) {
            throw IllegalArgumentException("$variableDec is not allowed")
        }
    }

    fun checkDataTypeIsOkWithExpression(exp: Expression, dataType: String) {
        // TODO(How to check that expression is of x type???)
    }

    fun checkVariableName(name: String) {
        val variableNamePattern = Regex("^[a-zA-Z_][a-zA-Z0-9_]*$")
        if (variableNamePattern.matches(name)) {
            return
        }
        throw IllegalArgumentException("$name is not allowed")
    }
}
