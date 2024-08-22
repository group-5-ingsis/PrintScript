package interpreter

import parser.SyntacticParser
import visitor.NodeVisitor

object Interpreter {
  fun interpret(rootAstNode: SyntacticParser.RootNode) {
    val nodeVisitor = NodeVisitor()
    rootAstNode.accept(nodeVisitor)
  }
}
