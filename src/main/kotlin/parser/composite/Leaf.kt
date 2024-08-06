package parser.composite

import Position
import token.TokenType
import kotlin.jvm.Throws

class Leaf(private val type: TokenType, private val value: String, private val location: Position): Node {

  override fun addChild(node: Node) {
    throw IllegalArgumentException("Illegal operation for leaf node")
  }

  @Throws(IllegalArgumentException::class)
  override fun removeChild(node: Node) {
    throw IllegalArgumentException("Illegal operation for leaf node")
  }

  override fun solve() {
    TODO("Not yet implemented")
  }
}