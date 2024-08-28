package parser

import SemanticErrorException
import token.Token


// Singleton object since it can be reused with different arguments.
class Parser {

    @Throws(SemanticErrorException::class)
    fun run(tokens: List<Token>): SyntacticParser.RootNode {
        return SyntacticParser(tokens).parse()
    }
}
