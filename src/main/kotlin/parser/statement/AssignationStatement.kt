package parser.statement

class AssignationStatement: StatementType {

    override fun isType(statement: Statement): Boolean {
        val content = statement.content

        val firstToken = content[0]
        val secondToken = content[1]
        val thirdToken = content[2]

        val isLeafNode = (
                   thirdToken.type == "NUMBER"
                || thirdToken.type == "STRING"
                || thirdToken.type == "IDENTIFIER")

        return (firstToken.type == "IDENTIFIER") &&
                (secondToken.type == "ASSIGNMENT") &&
                (isLeafNode)
    }
}