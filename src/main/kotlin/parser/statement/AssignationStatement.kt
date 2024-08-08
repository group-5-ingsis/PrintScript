package parser.statement

class AssignationStatement: StatementType {

    override fun isType(statement: Statement): Boolean {
        val content = statement.content

        val firstToken = content[0]
        val secondToken = content[1]
        val thirdToken = content[2]

        return (firstToken.type == "VARIABLE_NAME") &&
                (secondToken.type == "ASSIGNMENT") &&
                (thirdToken.type == "NUMBER")
    }
}