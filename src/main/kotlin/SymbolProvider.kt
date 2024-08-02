package org.example

object SymbolProvider {
    fun getKeywordMatrix(input: String): List<List<String>> {
        // Calls a new method to hide implementation details
        return getSymbolsFor(input)
    }

    private fun getSymbolsFor(input: String): List<List<String>> {
        // If the input is empty, return an empty list
        if (input.isEmpty()) return listOf()

        // Replace newlines with spaces
        val adjustedInput: List<String> = input.split("\n")

        // Regular expression to match quoted text with quotes, words, and semicolons
        val regex = Regex("\"[^\"]*\"|[\\w]+|[;\\-+*/=]")

        // List to store matches
        val matches: MutableList<List<String>> = mutableListOf()

        // Find all matches and add them to the list
        for (i in adjustedInput) {
            matches.add(regex.findAll(i).map { it.value }.toList())
        }

        return matches;
    }
    /*
    EXPLANATION:

        Get the keyword matrix (all keywords with their positions represented
        by their index in the list of lists) where index [0][0] is line 1 symbol 1.

        ej: getKeywordMatrix(" let a : String = "hola";
                               let b : String = "chau";")

            returns -> [["let", "a", ":", "String", "=", ""hola"", ";"],
                        ["let", "b", ":", "String", "=", ""chau"", ";"]]

            matrix[0][5] = ""hola""
     */
}