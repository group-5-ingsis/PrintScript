package token

object SymbolProvider {
    /**
     * Extracts a matrix of keywords from the given input string.
     *
     * @param input The input string to process. Expected to contain symbols or keywords possibly enclosed in quotes
     *              or represented by special characters.
     * @return A list of lists of strings, where each inner list represents the symbols or keywords identified in an
     *         individual line of the input. Lines with no content will result in empty lists.
     */
    fun getKeywordMatrix(input: String): List<List<String>> {
        return getSymbolsFor(input)
    }

    private fun getSymbolsFor(input: String): List<List<String>> {
        if (input.isEmpty()) return listOf()

        val adjustedInput: List<String> = input.split("\n")

        val regex = Regex("'[^']*'|\"[^\"]*\"|\\d+(\\.\\d+)?|\\w+|[;@\\-+*/=:(){},!#$%^&_|~.`\\[\\]<>?]")

        val matches: MutableList<List<String>> = mutableListOf()

        for (i in adjustedInput) {
            val matchesInString = regex.findAll(i).map { it.value }.toList()

            matches.add(matchesInString)
        }

        return matches
    }
}
