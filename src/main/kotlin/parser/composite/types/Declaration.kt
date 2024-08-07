package parser.composite.types

import parser.composite.Node

class Declaration(private val type: String, private val identifier: String) : Node {
  private val children: MutableList<Node> = mutableListOf()

  override fun addChild(node: Node) {
    children.add(node)
  }

  override fun removeChild(node: Node) {
    children.remove(node)
  }

  override fun solve() {

    /* Consigna:
          Para hacer más cómoda la experiencia del usuario del CLI, mientras se realiza el proceso de
          “parsing” del archivo se debe ir mostrando en pantalla el progreso.
    */
    println("Declared variable '${identifier}' as type $type")
  }
}