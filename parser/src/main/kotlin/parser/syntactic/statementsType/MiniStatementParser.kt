package parser.syntactic.statementsType
import nodes.StatementType
import token.Token

typealias ParseStatementResult = Pair<List<Token>, StatementType>

interface MiniStatementParser {

    fun parse(tokens: List<Token>): ParseStatementResult
}
