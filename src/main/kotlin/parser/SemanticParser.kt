package parser

import parser.composite.Node
import kotlin.jvm.Throws

class SemanticParser {

    @Throws(SemanticErrorException::class)
    fun run(ast: Node): Node {
        return validateAST(ast)
    }

    @Throws(SemanticErrorException::class)
    private fun validateAST(ast: Node): Node {
        /* Logic for validating semantically */
        return ast
    }
}