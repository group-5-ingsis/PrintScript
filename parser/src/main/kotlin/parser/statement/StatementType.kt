package parser.statement

import token.Token

class StatementType(private val elements: List<TokensNamesForStatements>, val  name: String) {

    init {
        addThisStatementToAllExistingStatementList()
    }

    private fun addThisStatementToAllExistingStatementList(){
        if (allExistingStatements.any { it.name == name }) {
            return
        }
        allExistingStatements.add(this)
    }


    fun isType(statement: Statement): Boolean{

        if (statement.content.size < elements.size) return false

        val content = statement.content

        for (i in elements.indices) {
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
        val allExistingStatements: MutableSet<StatementType> = mutableSetOf()

        init {

            addAssignDeclare()
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
            allExistingStatements.add(StatementType(listOf(), "Unknown"))

        }

        private fun addAssignDeclare() {
            allExistingStatements.add(
                StatementType(
                    listOf(
                        TokensNamesForStatements.SingleName("DECLARATION_KEYWORD"),
                        TokensNamesForStatements.SingleName("IDENTIFIER"),
                        TokensNamesForStatements.SingleName("PUNCTUATION"),
                        TokensNamesForStatements.SingleName("VARIABLE_TYPE"),
                        TokensNamesForStatements.SingleName("ASSIGNMENT"),
                        TokensNamesForStatements.MultipleNames(listOf("NUMBER", "STRING", "IDENTIFIER")),
                        TokensNamesForStatements.SingleName("PUNCTUATION"),
                        ), "AssignDeclare"
                )
            )
        }


        sealed class TokensNamesForStatements {
            data class SingleName(val value: String) : TokensNamesForStatements()
            data class MultipleNames(val values: List<String>) : TokensNamesForStatements()
        }

        fun getStatementNameCorrespondingToTokens(tokens : List<Token>): String {
            for (aStatement in allExistingStatements){
                if (aStatement.isType(Statement(tokens, ""))){
                    return aStatement.name
                }
            }
            return "Unknown"
        }

        fun getAllowedStatements() : Set<StatementType>{
            return allExistingStatements
        }





    }
    }



