package parser.statement


class AssignationStatement: StatementType {

    override fun isType(statement: Statement): Boolean {

        val content = statement.content

        val firstToken = content[0]
        val secondToken = content[1]
        val thirdToken = content[2]
        val fourthToken = content.getOrNull(3)

        val numOfTokens = content.size

        return (numOfTokens >= 3) &&
                (firstToken.type == "IDENTIFIER") &&
                (secondToken.type == "PUNCTUATION") &&
                (thirdToken.type == "VARIABLE_TYPE") &&
                (fourthToken?.type == "ASSIGNMENT")
    }

}