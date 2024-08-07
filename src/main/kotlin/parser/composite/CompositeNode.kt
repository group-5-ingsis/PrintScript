package parser.composite

import Position

/* TODO: delete after. Class works as example for Composite operations. */
class CompositeNode(private val type: String, private val value: String, private val location: Position) : Node {
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