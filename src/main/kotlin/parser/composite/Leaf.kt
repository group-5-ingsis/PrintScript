package parser.composite

import Position
import token.TokenType

class Leaf(private val type: TokenType, private val location: Position) : Node {
    private val children: MutableList<Node> = mutableListOf()

    override fun addChild(node: Node) {
        children.add(node)
    }

    override fun removeChild(node: Node) {
        children.remove(node)
    }

    override fun solve(): Void {
        TODO("Not yet implemented")
    }
}