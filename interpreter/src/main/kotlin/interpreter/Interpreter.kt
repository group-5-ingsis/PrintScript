package interpreter

import environment.Environment
import nodes.Statement
import visitor.NodeVisitor
import visitor.statementVisitorResult

object Interpreter {

    fun interpret(
        statement: Statement,
        version: String = "1.1",
        scope: Environment,
        readInput: String? = null,
        stringBuilder: StringBuilder = StringBuilder()
    ): statementVisitorResult {
        val nodeVisitor = NodeVisitor()
        val result = statement.accept(nodeVisitor)
        val printOutput = result.first
        val newScope = result.second
        return Pair(printOutput, newScope)
    }
}
