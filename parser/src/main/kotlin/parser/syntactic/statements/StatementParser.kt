package parser.syntactic.statements
import nodes.Statement
import parser.syntactic.TokenManager
import token.Token

typealias ParseStatementResult = Pair<List<Token>, Statement>

interface StatementParser {
    fun parse(manager: TokenManager): Statement
}
