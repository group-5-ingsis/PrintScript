package interpreter

import parser.SyntacticParser
import visitor.NodeVisitor

object Interpreter {
  fun interpret(rootAstNode: SyntacticParser.RootNode): String {
    val nodeVisitor = NodeVisitor()
    rootAstNode.accept(nodeVisitor)
    return nodeVisitor.getOutput()
  }
}
