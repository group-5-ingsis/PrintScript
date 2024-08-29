package interpreter

import parser.SyntacticParser
import visitor.NodeVisitor

object Interpreter {

    fun interpret(rootAstNode: SyntacticParser.RootNode): String {
        rootAstNode.accept(NodeVisitor)
        val output = NodeVisitor.getOutput()
        return output
    }
}
