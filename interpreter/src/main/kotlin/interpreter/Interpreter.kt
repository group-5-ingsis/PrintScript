package interpreter

import parser.SyntacticParser
import visitor.NodeVisitor
import visitor.VariableTable

object Interpreter {

    private val nodeVisitor = NodeVisitor(VariableTable)

    fun interpret(rootAstNode: SyntacticParser.RootNode){
        rootAstNode.accept(nodeVisitor)
    }

    fun getVariableTable(): VariableTable {
        return VariableTable
    }
}