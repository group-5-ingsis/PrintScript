package interpreter

import Environment
import nodes.StatementType
import position.visitor.InputProvider
import position.visitor.PrintScriptInputProvider
import position.visitor.StatementVisitor
import position.visitor.statementVisitorResult

object Interpreter {

    fun interpret(
        statement: StatementType,
        version: String = "1.1",
        scope: Environment,
        stringBuilder: StringBuilder = StringBuilder(),
        inputProvider: InputProvider = PrintScriptInputProvider()
    ): statementVisitorResult {
        val nodeVisitor = StatementVisitor(inputProvider)
        val result = statement.acceptVisitor(nodeVisitor, scope, stringBuilder)
        val printOutput = result.first
        val newScope = result.second
        return Pair(printOutput, newScope)
    }
}
