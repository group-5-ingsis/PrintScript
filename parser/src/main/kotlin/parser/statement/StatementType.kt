package parser.statement

class StatementType(private val elements: List<TokensNamesForStatements>, val  name: String) {

    init {
        addThisStatementToAllExistingStatementList()
    }



    private fun addThisStatementToAllExistingStatementList(){
        val newList = allExistingStatements.toList()
        for(statement in newList){
            if (statement.name == name){
                return
            }
        }
        allExistingStatements.add(this)

        }


    fun isType(statement: Statement): Boolean{

        if (statement.content.size != elements.size) return false

        val content = statement.content

        for (i in content.indices) {
            when (val element = elements[i]) {
                is TokensNamesForStatements.MultipleNames -> {
                    // Check if any of the names in the list match the content at index i
                    val matchFound = element.values.any { it == content[i].type }
                    if (!matchFound) return false
                }
                is TokensNamesForStatements.SingleName -> {
                    // Check if the single name matches the content at index i
                    if (element.value != content[i].type) return false
                }

            }
        }
        return true

    }
    companion object {
        val allExistingStatements: MutableList<StatementType> = mutableListOf()

        init {
            allExistingStatements.add(StatementType(listOf(), "Unknown"))
            allExistingStatements.add(StatementType(
                listOf(
                    TokensNamesForStatements.SingleName("KEYWORD"),
                    TokensNamesForStatements.SingleName("VARIABLE_NAME"),
                    TokensNamesForStatements.SingleName("PUNCTUATION"),
                    TokensNamesForStatements.SingleName("VARIABLE_TYPE"),
                    TokensNamesForStatements.SingleName("ASSIGNMENT"),
                    TokensNamesForStatements.MultipleNames(listOf("NUMBER", "STRING", "IDENTIFIER"))
                ), "AssignationDeclaration")
            )
            allExistingStatements.add(StatementType(
                listOf(
                    TokensNamesForStatements.SingleName("DECLARATION_KEYWORD"),
                    TokensNamesForStatements.SingleName("IDENTIFIER"),
                    TokensNamesForStatements.SingleName("PUNCTUATION"),
                    TokensNamesForStatements.SingleName("VARIABLE_TYPE")
                ),
                "Declaration"
            ))
            allExistingStatements.add( StatementType(
                listOf(
                    TokensNamesForStatements.SingleName("IDENTIFIER"),
                    TokensNamesForStatements.SingleName("ASSIGNMENT"),
                    TokensNamesForStatements.MultipleNames(listOf("NUMBER", "STRING", "IDENTIFIER"))
                ),
                "Assignation"
            ))

        }


        sealed class TokensNamesForStatements {
            data class SingleName(val value: String) : TokensNamesForStatements()
            data class MultipleNames(val values: List<String>) : TokensNamesForStatements()
        }

        fun getStatementName(thisStatement: Statement): String {
            for (aStatement in allExistingStatements){
                if (aStatement.isType(thisStatement)){
                    return aStatement.name
                }
            }
            return "Unknown"
        }

        fun getAllowedStatements() : List<StatementType>{
            return allExistingStatements
        }

        fun categorize(statements: List<Statement>) : List<Statement> {
            val categorizedStatements = mutableListOf<Statement>()
            val newList = allExistingStatements.toList()

            for (statement in statements) {
                for (allowedStatement in newList) {
                    val isType = allowedStatement.isType(statement)
                    if (isType) {
                        statement.statementType = allowedStatement.name
                        categorizedStatements.add(statement)
                    }
                }
            }
            return categorizedStatements
        }



    }
    }



