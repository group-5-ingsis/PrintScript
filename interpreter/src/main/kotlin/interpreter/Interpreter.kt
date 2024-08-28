package interpreter

import Environment
import parser.SyntacticParser
import visitor.NodeVisitor

object Interpreter {

    fun interpret(rootAstNode: SyntacticParser.RootNode): String {
        val scope = Environment()
        val nodeVisitor = NodeVisitor(scope)
        rootAstNode.accept(nodeVisitor)
        val output = nodeVisitor.getOutput()
        return output
    }
}
