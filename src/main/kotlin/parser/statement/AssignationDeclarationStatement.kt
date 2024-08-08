package parser.statement


class AssignationDeclarationStatement: StatementType {

    override fun isType(statement: Statement): Boolean {

        val content = statement.content

        if (content.size < 6) return false

        val firstToken = content[0]
        val secondToken = content[1]
        val thirdToken = content[2]
        val fourthToken = content[3]
        val fifthToken = content[4]
        val sixthToken = content[5]

        return (firstToken.type == "KEYWORD") &&
                (secondToken.type == "VARIABLE_NAME") &&
                (thirdToken.type == "PUNCTUATION") &&
                (fourthToken.type == "VARIABLE_TYPE") &&
                (fifthToken.type == "ASSIGNMENT") &&
                (sixthToken.type == "NUMBER")
    }

}