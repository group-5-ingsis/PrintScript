package token

object TokenTypesMapGenerator {
  fun getTypesMap(version: String): Map<String, String> {
    return getDefaultPatternsMap() + when (version) {
      "1.0" -> getVersionPatterns("1.0")
      "1.1" -> getVersionPatterns("1.1")
      else -> getDefaultPatternsMap()
    }
  }

  private fun getDefaultPatternsMap(): Map<String, String> {
    return mapOf(
      "=" to "ASSIGNMENT",
      """^-?\d+(\.\d+)?$""" to "NUMBER",
      """^(['"]).*\1$""" to "STRING",
      """^[,;.(){}:\"\']$""" to "PUNCTUATION"
    )
  }

  private fun getVersionPatterns(version: String): Map<String, String> {
    return when (version) {
      "1.0" -> mapOf(
        """[+\-*/]""" to "OPERATOR",
        """let""" to "DECLARATION_KEYWORD",
        """string|number""" to "VARIABLE_TYPE",
        """println""" to "METHOD"
      )
      "1.1" -> mapOf(
        """[+\-*/]""" to "OPERATOR",
        """let|const""" to "DECLARATION_KEYWORD",
        """string|number|boolean""" to "VARIABLE_TYPE",
        """println|readInput""" to "METHOD",
        """if|else""" to "CONDITIONAL",
        """\{|\}""" to "SCOPE"
      )
      else -> emptyMap()
    }
  }
}
