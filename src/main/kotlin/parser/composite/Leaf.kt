package parser.composite

import Position
import token.TokenType

class Leaf(private val type: TokenType, private val location: Position): Node {

    override fun addChild(node: Node) { }

    override fun removeChild(node: Node) { }

    override fun solve() {
        TODO("Not yet implemented")
    }
}