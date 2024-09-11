package linter

import nodes.Expression
import nodes.StatementType
import position.Position
import position.visitor.Visitor
import rules.LinterRules

class LinterVisitor(private val linterRules: LinterRules) : Visitor {

    private lateinit var linterResult: LinterResult

    private val rulesMap = linterRules.getAsMap()

    private fun isLinterResultInitialized(): Boolean {
        return this::linterResult.isInitialized
    }

    fun getLinterResult(): LinterResult {
        if (!isLinterResultInitialized()) {
            throw IllegalStateException("Cannot call getLinterResult() before linting")
        }
        return linterResult
    }

    override fun visitPrintStm(statement: StatementType.Print) {
        val expression = statement.value.expression
        val position = statement.position
        val expressionType = expression.expressionType

        if (rulesMap["printlnExpressionAllowed"] == false) {
            if (expressionType != "VARIABLE_EXPRESSION" && expressionType != "LITERAL_EXPRESSION") {
                linterResult = LinterResult(
                    false,
                    "println() statements must receive a literal or identifier expression at $position, got $expressionType."
                )
                return
            }
        }

        linterResult = LinterResult(true, "No errors found at $position")
    }

    override fun visitExpressionStm(statement: StatementType.StatementExpression) {
        TODO("Not yet implemented")
    }

    override fun visitVariableStm(statement: StatementType.Variable) {
        val identifier: String = statement.identifier
        val position: Position = statement.position
        when (rulesMap["identifierNamingConvention"]) {
            "camel-case" -> {
                if (!identifier.matches(Regex("^[a-z]+(?:[A-Z][a-z0-9]*)*$"))) {
                    linterResult = LinterResult(
                        false,
                        "Variable names must be in camelCase at $position, got $identifier."
                    )
                    return
                }
            }
            "snake-case" -> {
                if (!identifier.matches(Regex("^[a-z]+(_[a-z]+)*$"))) {
                    linterResult = LinterResult(
                        false,
                        "Variable names must be in snake_case at $position, got $identifier."
                    )
                    return
                }
            }
            "off" -> {
                linterResult = LinterResult(true, "No errors found at $position")
            }
            else -> {
                throw IllegalArgumentException("Invalid identifier naming convention ${rulesMap["identifierNamingConvention"]}, expected camel-case, snake-case or off.")
            }
        }
        linterResult = LinterResult(true, "No errors found at $position")
    }

    override fun visitBlockStm(statement: StatementType.BlockStatement) {
        TODO("Not yet implemented")
    }

    override fun visitIfStm(statement: StatementType.IfStatement) {
        TODO("Not yet implemented")
    }

    override fun visitVariable(expression: Expression.Variable) {
        val position = expression.position
        linterResult = LinterResult(true, "No errors found at $position")
    }

    override fun visitAssign(expression: Expression.Assign) {
        val position = expression.position
        linterResult = LinterResult(true, "No errors found at $position")
    }

    override fun visitBinary(expression: Expression.Binary) {
        val position = expression.position
        linterResult = LinterResult(true, "No errors found at $position")
    }

    override fun visitGrouping(expression: Expression.Grouping) {
        val position = expression.position
        linterResult = LinterResult(true, "No errors found at $position")
    }

    override fun visitLiteral(expression: Expression.Literal) {
        val position = expression.position
        linterResult = LinterResult(true, "No errors found at $position")
    }

    override fun visitUnary(expression: Expression.Unary) {
        val position = expression.position
        linterResult = LinterResult(true, "No errors found at $position")
    }

    override fun visitIdentifier(expression: Expression.IdentifierExpression) {
        val position = expression.position
        linterResult = LinterResult(true, "No errors found at $position")
    }
}
