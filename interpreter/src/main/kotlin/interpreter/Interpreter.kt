package interpreter

import Environment
import parser.SyntacticParser
import visitor.NodeVisitor

object Interpreter {

    fun interpret(rootAstNode: SyntacticParser.RootNode, scope: Environment): String {

        val nodeVisitor = NodeVisitor(scope)
        rootAstNode.accept(nodeVisitor)
        val output = nodeVisitor.getOutput()
        return output
    }
}
