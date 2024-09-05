package parser

import parser.semantic.SemanticParser
import parser.syntactic.SyntacticParser
import token.Token

class Parser(private val lexer: Iterator<Token>) : Iterator<SyntacticParser.RootNode> {
    private val tokens = mutableListOf<Token>()
    private var currentNode: SyntacticParser.RootNode? = null

    override fun hasNext(): Boolean {
        return lexer.hasNext() || tokens.isNotEmpty() || currentNode != null
    }

    override fun next(): SyntacticParser.RootNode {
        if (currentNode != null) {
            val node = currentNode
            currentNode = null
            return node ?: throw NoSuchElementException("No ASTNode formed")
        }

        while (lexer.hasNext()) {
            val token = lexer.next()
            tokens.add(token)

            if (token.value == ";") {
                currentNode = parseStatement(tokens)
                tokens.clear()
                return currentNode ?: throw NoSuchElementException("No ASTNode formed")
            }
        }

//        if (tokens.isNotEmpty()) {
//            currentNode = parseStatement(tokens)
//            tokens.clear()
//            return currentNode ?: throw NoSuchElementException("No ASTNode formed")
//        }

        throw NoSuchElementException("No more tokens left.")
    }

    private fun parseStatement(tokens: List<Token>): SyntacticParser.RootNode {
        if (tokens.isEmpty()) {
            throw IllegalArgumentException("Cannot parse an empty token list")
        }

        val astNode = SyntacticParser.parse(tokens)
        return SemanticParser.validate(astNode)
    }
}
