package parser.syntactic.statements
import nodes.Statement
import token.Token

interface StatementParser {
  fun parse(tokens: List<Token>): Statement
}
