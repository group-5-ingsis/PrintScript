package parser.syntactic

import nodes.Statement
import token.Token

object SyntacticParser {

    fun parse(tokens: List<Token>, version: String): Statement {
        val statementParser = ParserFactory.createParser(tokens, version)
        val node = statementParser.parse(tokens)
        return node
    }
}
