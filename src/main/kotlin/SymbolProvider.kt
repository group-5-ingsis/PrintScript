object SymbolProvider {
    fun getKeywordMatrix(input: String): List<List<String>> {
        return getSymbolsFor(input)
    }

    private fun getSymbolsFor(input: String): List<List<String>> {
        if (input.isEmpty()) return listOf()

        val adjustedInput: List<String> = input.split("\n")

        val regex = Regex("\'[^\']*\"\'|\"[^\"]*\"|[\\w]+|[;\\-+*/=:(){},.]")

        val matches: MutableList<List<String>> = mutableListOf()

        for (i in adjustedInput) {
            matches.add(regex.findAll(i).map { it.value }.toList())
        }

        return matches
    }
}