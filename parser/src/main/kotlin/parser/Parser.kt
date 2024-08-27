package parser

import parser.exceptions.SemanticErrorException
import token.Token

// Singleton object since it can be reused with different arguments.
class Parser {
    private val syntacticParser = SyntacticParser()

    @Throws(SemanticErrorException::class)
    fun run(tokens: List<Token>): SyntacticParser.RootNode {
        return syntacticParser.run(tokens)
    }
}
