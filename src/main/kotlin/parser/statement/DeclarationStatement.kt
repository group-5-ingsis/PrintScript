package parser.statement

class DeclarationStatement : StatementType {

  override fun isType(statement: Statement): Boolean {
    val content = statement.content
    val firstToken = content[0]
    val secondToken = content[1]
    val thirdToken = content[2]
    val fourthToken = content[3]

    return (firstToken.type == "DECLARATION_KEYWORD") &&
            (secondToken.type == "IDENTIFIER") &&
            (thirdToken.type == "PUNCTUATION") &&
            (fourthToken.type == "VARIABLE_TYPE")
  }

  override fun toString(): String {
    return "Declaration"
  }
}