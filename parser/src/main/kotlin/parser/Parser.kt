package parser

import parser.semantic.SemanticParser
import parser.syntactic.SyntacticParser
import token.Token

class Parser(private val lexer: Iterator<Token>) : Iterator<SyntacticParser.RootNode> {

    override fun hasNext(): Boolean {
        return lexer.hasNext()
    }

    override fun next(): SyntacticParser.RootNode {
        val node = parseTokens(emptyList())
        return node ?: throw NoSuchElementException("No ASTNode formed")
    }

    private fun parseTokens(tokens: List<Token>): SyntacticParser.RootNode? {
        if (!lexer.hasNext() && tokens.isNotEmpty()) {
            return parseStatement(tokens)
        }

        if (!lexer.hasNext()) {
            return null
        }

        val token = lexer.next()
        val newTokens = tokens + token

        return if (token.value == ";") {
            parseStatement(newTokens)
        } else {
            parseTokens(newTokens)
        }
    }

    private fun parseStatement(tokens: List<Token>): SyntacticParser.RootNode {
        if (tokens.isEmpty()) {
            throw IllegalArgumentException("Cannot parse an empty token list")
        }

        val astNode = SyntacticParser.parse(tokens)
        return SemanticParser.validate(astNode)
    }
}
