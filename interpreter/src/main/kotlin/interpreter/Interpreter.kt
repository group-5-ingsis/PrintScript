package interpreter

import Environment
import nodes.StatementType
import position.visitor.StatementVisitor
import position.visitor.statementVisitorResult

class Interpreter() {

    fun interpret(scope: Environment, parser: Iterator<StatementType>, sb: StringBuilder): statementVisitorResult {
        var currentScope = scope
        var stB = StringBuilder(sb.toString())
        while (parser.hasNext()) {
            val statement = parser.next()
            val nodeVisitor = StatementVisitor()
            val result = statement.acceptVisitor(nodeVisitor, currentScope, sb)
            currentScope = result.second
            stB = result.first
        }

        return Pair(stB, currentScope)
    }
}
