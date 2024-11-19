package interpreter

import environment.Environment
import nodes.Statement
import utils.InputProvider

typealias statementVisitorResult = Pair<StringBuilder, Environment>

object Interpreter {

  fun interpret(
    statement: Statement,
    version: String = "1.1",
    scope: Environment,
    inputProvider: InputProvider = InputProvider()
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
