package parser.syntactic.statements
import nodes.Statement
import token.Token

typealias ParseStatementResult = Pair<List<Token>, Statement>

interface StatementParser {

    fun parse(tokens: List<Token>): ParseStatementResult
}
