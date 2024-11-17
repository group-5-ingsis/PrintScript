package linter

import nodes.Expression
import nodes.Statement
import rules.LinterRules
import token.Position
import visitor.Visitor

class LinterVisitor(linterRules: LinterRules) : Visitor<Unit> {

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

  override fun visitPrint(statement: Statement.Print) {
    val expression = statement.value.expression
    val position = statement.position
    val expressionType = expression.expressionType
    checkExpressionArgs(expressionType, position, "printlnExpressionAllowed", "println()")
  }

  private fun checkExpressionArgs(expressionType: String, position: Position, ruleName: String, statementName: String) {
    if (rulesMap[ruleName] == false) {
      if (expressionType != "VARIABLE_EXPRESSION" && expressionType != "LITERAL_EXPRESSION") {
        linterResult = LinterResult(
          false,
          "$statementName statements must receive a literal or identifier, not an expression. At $position, got $expressionType."
        )
        return
      }
    }

    linterResult = LinterResult(true, "No errors found at $position")
  }

  override fun visitExpression(statement: Statement.StatementExpression) {
    if (statement.value.expressionType == "READ_INPUT") {
      statement.value.accept(this)
    } else {
      val position = statement.position
      linterResult = LinterResult(true, "No errors found at $position")
    }
  }

  override fun visitVariableStatement(statement: Statement.Variable) {
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
    if (statement.initializer != null) {
      if (statement.initializer!!.expressionType == "READ_INPUT") {
        statement.initializer!!.accept(this)
      }
    }
  }

  override fun visitBlock(statement: Statement.BlockStatement) {
    statement.listStm.forEach {
      it.accept(this)
    }
  }

  override fun visitIf(statement: Statement.IfStatement) {
    statement.thenBranch.accept(this)
    if (statement.elseBranch != null) {
      statement.elseBranch!!.accept(this)
    }
  }

  override fun visitVariableExpression(expression: Expression.Variable) {
    linterResult = LinterResult(true, "No errors found at ${expression.position}")
  }

  override fun visitAssign(expression: Expression.Assign) {
    linterResult = LinterResult(true, "No errors found at ${expression.position}")
  }

  override fun visitBinary(expression: Expression.Binary) {
    linterResult = LinterResult(true, "No errors found at ${expression.position}")
  }

  override fun visitGrouping(expression: Expression.Grouping) {
    linterResult = LinterResult(true, "No errors found at ${expression.position}")
  }

  override fun visitLiteral(expression: Expression.Literal) {
    linterResult = LinterResult(true, "No errors found at ${expression.position}")
  }

  override fun visitUnary(expression: Expression.Unary) {
    linterResult = LinterResult(true, "No errors found at ${expression.position}")
  }

  override fun visitIdentifier(expression: Expression.IdentifierExpression) {
    linterResult = LinterResult(true, "No errors found at ${expression.position}")
  }

  override fun visitReadInput(readInput: Expression.ReadInput) {
    val expression = readInput.value.expression
    val position = readInput.position
    val expressionType = expression.expressionType
    checkExpressionArgs(expressionType, position, "readInputExpressionAllowed", "readInput()")
  }

  override fun visitReadEnv(readEnv: Expression.ReadEnv) {
    linterResult = LinterResult(true, "No errors found at ${readEnv.position}")
  }
}
