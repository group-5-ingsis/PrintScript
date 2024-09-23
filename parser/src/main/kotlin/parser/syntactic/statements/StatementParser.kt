package parser.syntactic.statements
import nodes.StatementType
import token.Token

typealias ParseStatementResult = Pair<List<Token>, StatementType>

interface StatementParser {

    fun parse(tokens: List<Token>): ParseStatementResult
}
