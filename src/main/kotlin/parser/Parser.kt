package parser

import parser.composite.Node
import token.Token

class Parser {
    private val syntacticParser = SyntacticParser()
    private val semanticParser = SemanticParser()

    /* Performs a call to both parsers. */
    @Throws(SemanticErrorException::class)
    fun run(tokens: List<Token>): Node {
        val ast: Node = syntacticParser.run(tokens)
        return semanticParser.run(ast)
    }
}