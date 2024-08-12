package interpreter

import composite.Node
import visitor.NodeVisitor
import visitor.VariableTable

class Interpreter {

    private val variableTable = VariableTable()
    private val nodeVisitor = NodeVisitor(variableTable)

    fun interpret(rootAstNode: Node){
        rootAstNode.accept(nodeVisitor)
    }
}