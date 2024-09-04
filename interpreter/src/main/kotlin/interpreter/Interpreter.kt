package interpreter

import Environment
import parser.syntactic.SyntacticParser
import position.visitor.StatementVisitor

object Interpreter {

    fun interpret(rootAstNode: SyntacticParser.RootNode, scope: Environment): Environment {
        val nodeVisitor = StatementVisitor()
        return rootAstNode.accept(nodeVisitor, scope)
    }
}
