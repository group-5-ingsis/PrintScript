package parser.statement

class StatementType(private val elements: List<StatementManager.TokensNamesForStatements>, val name: String) {
    init {
        addThisStatementToAllExistingStatementList()
    }

    private fun addThisStatementToAllExistingStatementList() {
        if (StatementManager.allExistingStatements.any { it.name == name }) {
            return
        }
        StatementManager.allExistingStatements.add(this)
    }

    fun isType(statement: Statement): Boolean {
        if (statement.content.size < elements.size) return false

        val content = statement.content

        for (i in elements.indices) {
            when (val element = elements[i]) {
                is StatementManager.TokensNamesForStatements.MultipleNames -> {
                    // Check if any of the names in the list match the content at index i
                    val matchFound = element.values.any { it == content[i].type }
                    if (!matchFound) return false
                }
                is StatementManager.TokensNamesForStatements.SingleName -> {
                    // Check if the single name matches the content at index i
                    if (element.value != content[i].type) return false
                }
            }
        }
        return true
    }
}
