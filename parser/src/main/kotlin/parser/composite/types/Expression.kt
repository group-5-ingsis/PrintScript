package parser.composite.types

import interpreter.NodeVisitor
import parser.NodeResult
import parser.composite.Node

class Expression: Node {

    override fun solve(): NodeResult {
        TODO("Not yet implemented")
    }

    override fun accept(visitor: NodeVisitor): NodeResult {
        TODO("Not yet implemented")
    }
}