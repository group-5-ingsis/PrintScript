package interpreter

import environment.Environment
import nodes.StatementType
import visitor.*

object Interpreter {

  fun interpret(
    statement: StatementType,
    version: String = "1.1",
    scope: Environment,
    inputProvider: InputProvider = PrintScriptInputProvider()
  ): statementVisitorResult {
    val nodeVisitor = StatementVisitor(inputProvider)
    val result = statement.acceptVisitor(nodeVisitor, scope, StringBuilder())
    val printOutput = result.first
    val newScope = result.second
    return Pair(printOutput, newScope)
  }
}
