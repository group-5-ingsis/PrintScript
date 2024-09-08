package parser.syntactic.statements

import parser.syntactic.TokenManager
import token.Token

/**
 * their corresponding parsers (`MiniStatementParser`) to determine how to parse the tokens.
 *
 * @property nextStatementsList A list of pairs, where each pair contains:
 *  - `String`: The expected type of statement as a string (e.g., "DECLARATION", "ASSIGNATION").
 *  - `MiniStatementParser`: The parser that should be used if the token matches the expected statement type.
 */
class GenericStatementParser(private val nextStatementsList: List<Pair<String, StatementParser>>) : StatementParser {

    /**
     * Parses a list of tokens and determines which type of statement it represents, then
     * delegates the parsing to the appropriate `MiniStatementParser` based on the next token.
     *
     * The function iterates through the `nextStatementsList` to find a matching statement type.
     * If a match is found, it uses the corresponding `MiniStatementParser` to parse the tokens.
     * If no match is found, the function defaults to using the last parser in the `nextStatementsList`.
     *
     * @param tokens The list of tokens to be parsed.
     * @return A `ParseStatementResult` object containing the result of the parsing process.
     * @throws NoSuchElementException if the token list is empty and no match is found.
     */

    override fun parse(tokens: List<Token>): ParseStatementResult {
        val manager = TokenManager(tokens) // Initialize the TokenManager with the provided tokens

        // Iterate through the list of statement types and their corresponding parsers
        // TODO change this logic. Consume one token at a time until it matches an expression/statement type.
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
        fun makeStatementParser(): StatementParser {
            // TODO change this.
            val statement = GenericStatementParser(
                listOf(
                    Pair("PRINT", PrintStatementParser()),
                    Pair("", ExpressionStatementParser())
                )
            )

            val declarationAssignationStatement = GenericStatementParser(
                listOf(
                    Pair(
                        "DECLARATION_KEYWORD",
                        LetDeclarationParser()
                    ),
                    Pair(
                        "CONST",
                        ConstDeclarationParser()
                    ),
                    Pair("", statement)
                )
            )

            return declarationAssignationStatement
        }
    }
}
