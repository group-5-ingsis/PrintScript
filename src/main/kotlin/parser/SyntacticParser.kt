package parser

import Position
import parser.composite.Leaf
import parser.composite.Node
import token.Token
import token.TokenType

/* Singleton? TODO check */
class SyntacticParser {

    /* Client method for calls to the syntactic parser. */
    fun run(tokens: List<Token>): Node {
        return buildAST(tokens)
    }

    private fun buildAST(tokens: List<Token>): Node {
        return Leaf(TokenType.UNKNOWN, Position(-1, -1))
    }
}