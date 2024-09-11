package interpreter

import nodes.StatementType
import position.visitor.Environment
import position.visitor.StatementVisitor
import position.visitor.statementVisitorResult

object Interpreter {

    private val env = Environment()

    fun interpret(astIterator: Iterator<StatementType>, version: String = "1.1"): statementVisitorResult {
        var sb = StringBuilder()
        var currentScope = env
        while (astIterator.hasNext()) {
            val statement = astIterator.next()
            val nodeVisitor = StatementVisitor()
            val result = statement.acceptVisitor(nodeVisitor, currentScope, sb)
            currentScope = result.second
            sb = result.first
        }

        return Pair(sb, currentScope)
    }
}
