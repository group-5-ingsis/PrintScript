package interpreter

import environment.Environment
import nodes.Statement
import visitor.InputProvider
import visitor.InterpreterVisitor
import visitor.PrintScriptInputProvider

typealias statementVisitorResult = Pair<StringBuilder, Environment>

object Interpreter {

    fun interpret(
        statement: Statement,
        version: String = "1.1",
        scope: Environment,
        inputProvider: InputProvider = PrintScriptInputProvider()
    ): statementVisitorResult {
        var outputHandler = StringBuilder()
        val interpreterVisitor = InterpreterVisitor(scope, version, inputProvider)
        val result = statement.accept(interpreterVisitor)
        val printOutput = result.first
        outputHandler = outputHandler.append(printOutput)
        val newScope = result.second
        return Pair(outputHandler, newScope)
    }
}
