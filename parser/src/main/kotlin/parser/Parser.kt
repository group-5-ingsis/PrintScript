package parser

import Environment
import SemanticErrorException
import parser.validation.SemanticValidator
import token.Token


// Singleton object since it can be reused with different arguments.
class Parser {
    private val syntacticParser = SemanticValidator()

    @Throws(SemanticErrorException::class)
    fun run(tokens: List<Token>): SyntacticParser.RootNode {

        val ast = SyntacticParser(tokens).parse()

        return ast

    }

}
