package interpreter

import parser.SyntacticParser
import visitor.NodeVisitor

object Interpreter {

    private val nodeVisitor = NodeVisitor()

    fun interpret(rootAstNode: SyntacticParser.RootNode){
        rootAstNode.accept(nodeVisitor)
    }
}