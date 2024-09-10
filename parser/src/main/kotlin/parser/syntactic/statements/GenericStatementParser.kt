package parser.syntactic.statements

import parser.syntactic.TokenManager
import token.Token

/**
 * A generic statement parser that uses a list of statement types and their corresponding parsers to determine how to parse tokens.
 *
 * @property nextStatementsList A list of pairs where each pair contains:
 *  - `String`: The expected type of statement as a string (e.g., "DECLARATION", "ASSIGNATION").
 *  - `StatementParser`: The parser that should be used if the token matches the expected statement type.
 */
class GenericStatementParser(private val nextStatementsList: List<Pair<String, StatementParser>>) : StatementParser {

    /**
     * Parses a list of tokens and determines which type of statement it represents,
     * then delegates the parsing to the appropriate `StatementParser` based on the next token.
     *
     * @param tokens The list of tokens to be parsed.
     * @return A `ParseStatementResult` object containing the result of the parsing process.
     * @throws NoSuchElementException if the token list is empty and no match is found.
     */
    override fun parse(tokens: List<Token>): ParseStatementResult {
        val manager = TokenManager(tokens) // Initialize the TokenManager with the provided tokens

        // Iterate through the list of statement types and their corresponding parsers
        for ((statementType, typeOfStatements) in nextStatementsList) {
            // Check if the next token in the queue matches the expected statement type
            if (manager.nextTokenMatchesExpectedType(statementType)) {
                manager.advance()
                return typeOfStatements.parse(manager.getTokens()) // Parse the tokens using the matching parser
            }
        }

        // If no match is found, use the last parser in the list as a fallback
        val (_, typeOfStatements) = nextStatementsList.last()
        return typeOfStatements.parse(manager.getTokens())
    }

    companion object {

        fun makeStatementParser(version: String): StatementParser {
            return when (version) {
                "1.0" -> createV1Parser()
                "1.1" -> createV1_1Parser()
                else -> throw IllegalArgumentException("Unsupported version: $version")
            }
        }

        // Cambiar a que cada version use differentes Parsers
        private fun createV1Parser(): StatementParser {
            val statement = GenericStatementParser(
                listOf(
                    Pair("PRINT", PrintStatementParser()),
                    Pair("", ExpressionStatementParser())
                )
            )

            val declarationAssignationStatement = GenericStatementParser(
                listOf(
                    Pair("DECLARATION_KEYWORD", LetDeclarationParser()),
                    Pair("CONST", ConstDeclarationParser()),
                    Pair("", statement)
                )
            )

            return declarationAssignationStatement
        }

        private fun createV1_1Parser(): StatementParser {
            val statement = GenericStatementParser(
                listOf(
                    Pair("PRINT", PrintStatementParser()),
                    Pair("", ExpressionStatementParser())
                )
            )

            val declarationAssignationStatement = GenericStatementParser(
                listOf(
                    Pair("VAR", LetDeclarationParser()), // Changed keyword for version 1.1
                    Pair("CONST", ConstDeclarationParser()),
                    Pair("", statement)
                )
            )

            return declarationAssignationStatement
        }
    }
}
