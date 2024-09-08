package interpreter

import Environment
import nodes.StatementType
import position.visitor.StatementVisitor

class Interpreter(private val parser: Iterator<StatementType>) {

    fun interpret(scope: Environment): Environment {
        var currentScope = scope

        while (parser.hasNext()) {
            val statement = parser.next()
            val nodeVisitor = StatementVisitor()
            currentScope = statement.acceptVisitor(nodeVisitor, currentScope)
        }

        return currentScope
    }
}
