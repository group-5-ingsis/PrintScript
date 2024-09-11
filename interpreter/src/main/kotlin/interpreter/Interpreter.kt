package interpreter

import nodes.StatementType
import position.visitor.Environment
import position.visitor.StatementVisitor
import position.visitor.statementVisitorResult

object Interpreter {

    private val env = Environment()

    fun interpret(statement: StatementType, version: String = "1.1"): statementVisitorResult {
        val sb = StringBuilder()
        var currentScope = env

        val nodeVisitor = StatementVisitor()
        val result = statement.acceptVisitor(nodeVisitor, currentScope, sb)
        currentScope = result.second
        sb.append(result.first)

        return Pair(sb, currentScope)
    }
}
