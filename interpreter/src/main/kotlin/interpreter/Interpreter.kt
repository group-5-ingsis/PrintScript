package interpreter

import nodes.StatementType
import position.visitor.Environment
import position.visitor.StatementVisitor
import position.visitor.statementVisitorResult

object Interpreter {

    fun interpret(
        statement: StatementType,
        version: String = "1.1",
        scope: Environment,
        readInput: String? = null,
        stringBuilder: StringBuilder = StringBuilder()
    ): statementVisitorResult {
        val nodeVisitor = StatementVisitor(readInput)
        val result = statement.acceptVisitor(nodeVisitor, scope, stringBuilder)
        val printOutput = result.first
        val newScope = result.second
        return Pair(printOutput, newScope)
    }
}
