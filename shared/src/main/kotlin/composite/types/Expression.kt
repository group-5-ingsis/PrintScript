package composite.types

import composite.Node
import visitor.NodeResult
import visitor.NodeVisitor

class Expression: Node {

    override fun solve(): NodeResult {
        TODO("Not yet implemented")
    }

    override fun accept(visitor: NodeVisitor): NodeResult {
        TODO("Not yet implemented")
    }
}