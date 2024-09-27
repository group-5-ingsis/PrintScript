package parser.syntactic.statements

import nodes.Expression
import nodes.Statement
import parser.syntactic.ParserFactory
import parser.syntactic.TokenManager

class PrintStatementParser(val version: String) : StatementParser {
    override fun parse(manager: TokenManager): Statement {
        val position = manager.getPosition()
        val expressionParser = ParserFactory.createExpressionParser(manager, version)
        val expression = expressionParser.parse(manager)
        manager.consume(";")
        if (expression is Expression.Grouping) {
            return Statement.Print(expression, position)
        } else {
            throw Error("Expected a grouping expression at line: ${position.line}")
        }
    }
}
