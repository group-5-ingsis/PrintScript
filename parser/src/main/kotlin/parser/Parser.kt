package parser

import parser.exceptions.SemanticErrorException
import token.Token

class Parser {
    private val syntacticParser = SyntacticParser()

    @Throws(SemanticErrorException::class)
    fun run(tokens: List<Token>): SyntacticParser.RootNode {
        val astRootNode = syntacticParser.run(tokens)
        val semanticParser = SemanticParser()
        return semanticParser.run(astRootNode)
    }
}
