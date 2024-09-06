package interpreter

import Environment
import nodes.StatementType
import position.visitor.StatementVisitor

object Interpreter {

    fun interpret(statement: StatementType, scope: Environment): Environment {
        val nodeVisitor = StatementVisitor()
        return statement.acceptVisitor(nodeVisitor, scope)
    }
}
