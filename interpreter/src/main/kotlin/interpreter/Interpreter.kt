package interpreter

import environment.Environment
import nodes.StatementType
import visitor.InputProvider
import visitor.NodeVisitor
import visitor.PrintScriptInputProvider
import visitor.statementVisitorResult

object Interpreter {

    fun interpret(
        statement: StatementType,
        version: String = "1.1",
        scope: Environment,
        inputProvider: InputProvider = PrintScriptInputProvider()
    ): statementVisitorResult {
        val nodeVisitor = NodeVisitor(scope, version, inputProvider)
        val result = statement.accept(nodeVisitor)
        val printOutput = result.first
        val newScope = result.second
        return Pair(printOutput, newScope)
    }
}
