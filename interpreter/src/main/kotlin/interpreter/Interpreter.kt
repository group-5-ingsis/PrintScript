package interpreter

import parser.SyntacticParser
import visitor.NodeVisitor
import visitor.VariableTable

object Interpreter {

    private val variableTable = VariableTable()
    private val nodeVisitor = NodeVisitor(variableTable)

    fun interpret(rootAstNode: SyntacticParser.RootNode){
        rootAstNode.accept(nodeVisitor)
    }

    fun getVariableTable(): VariableTable {
        return variableTable
    }
}