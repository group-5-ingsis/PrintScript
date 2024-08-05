package parser.composite

import Position
import token.TokenType

class CompositeNode(private val type: TokenType, private val location: Position) : Node {
    private val children = mutableListOf<Node>()

    override fun addChild(node: Node) {
        children.add(node)
    }

    override fun removeChild(node: Node) {
        children.remove(node)
    }

    override fun solve() {
        for (child: Node in children) {
            child.solve()
        }
    }
}