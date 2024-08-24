package interpreter

import parser.SyntacticParser
import visitor.NodeVisitor

object Interpreter {
  fun interpret(rootAstNode: SyntacticParser.RootNode): String {
    val nodeVisitor = NodeVisitor()
    rootAstNode.accept(nodeVisitor)
    val output = nodeVisitor.getOutput()
    return output
  }
}
