package parser.composite.types

import interpreter.NodeVisitor
import parser.NodeResult
import parser.composite.Node

class Identifier(private var variableName: String): Node {

    override fun solve(): NodeResult {
        TODO("Not yet implemented")
    }

    override fun accept(visitor: NodeVisitor): NodeResult {
        TODO("Not yet implemented")
    }

    fun getName(): String {
        return variableName
    }

    fun setName(name: String) {
        variableName = name
    }
}