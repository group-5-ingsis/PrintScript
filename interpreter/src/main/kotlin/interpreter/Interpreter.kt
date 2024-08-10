package interpreter

import parser.composite.Node

class Interpreter {

    private val variableTable = VariableTable()
    private val nodeVisitor = NodeVisitor(variableTable)

    fun interpret(rootAstNode: Node){
        rootAstNode.accept(nodeVisitor)
    }
}