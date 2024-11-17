package formatter

import nodes.Expression
import nodes.Statement
import rules.FormattingRules
import rules.RuleApplier
import visitor.Visitor

class FormatterVisitor(
  private val rules: FormattingRules,
  private val version: String
) : Visitor<Unit> {

  private val output = StringBuilder()
  private val ruleApplier = RuleApplier(rules)
  private var currentIndent = 0

  fun getFormattedOutput(): String {
    return output.toString()
  }

  override fun visitPrint(statement: Statement.Print) {
    appendPrintKeyword()
    statement.value.accept(this)
    output.append(";\n")
    appendNewlines(rules.newlineAfterPrintln)
  }

  override fun visitReadInput(expression: Expression.ReadInput) {
    TODO("Not yet implemented")
  }

  override fun visitReadEnv(expression: Expression.ReadEnv) {
    TODO("Not yet implemented")
  }

  override fun visitExpression(statement: Statement.StatementExpression) {
    val expression = statement.value
    expression.accept(this)
  }

  override fun visitVariableStatement(statement: Statement.Variable) {
    appendVariableDeclaration(statement)
    appendVariableInitializer(statement.initializer)
    output.append(";\n")
  }

  private fun appendVariableInitializer(initializer: Expression?) {
    initializer?.accept(this)
  }

  override fun visitBlock(statement: Statement.BlockStatement) {
    if (version >= "1.1") {
      val statements = statement.listStm
      processBlockStatements(statements)
      finalizeBlockIndentation()
    }
  }

  private fun processBlockStatements(statements: List<Statement>) {
    statements.forEach {
      appendIndent()
      it.accept(this)
    }
  }

  private fun finalizeBlockIndentation() {
    currentIndent -= rules.blockIndentation
    appendIndent()
  }

  override fun visitIf(statement: Statement.IfStatement) {
    handleIfCondition(statement.condition)
    handleThenBranch(statement.thenBranch)
    handleElseBranch(statement.elseBranch)
  }

  private fun handleIfCondition(condition: Expression) {
    output.append("if (")
    condition.accept(this)
    output.append(")")
    appendBracesForCondition()
  }

  private fun appendBracesForCondition() {
    if (rules.ifBraceSameLine) {
      output.append(" {\n")
    } else {
      output.append("\n")
      appendIndent()
      output.append("{\n")
    }
    currentIndent += rules.blockIndentation
  }

  private fun handleThenBranch(thenBranch: Statement) {
    thenBranch.accept(this)
    currentIndent -= rules.blockIndentation
    appendIndent()
    output.append("}")
  }

  private fun handleElseBranch(elseBranch: Statement?) {
    elseBranch?.let {
      output.append(" else")
      appendBracesForCondition()
      currentIndent += rules.blockIndentation
      elseBranch.accept(this)
      currentIndent -= rules.blockIndentation
      appendIndent()
      output.append("}\n")
    }
    output.append("\n")
  }

  private fun appendPrintKeyword() {
    val spaceSeparator = if (rules.singleSpaceSeparation) " " else ""
    output.append("println$spaceSeparator")
  }

  private fun appendNewlines(count: Int) {
    repeat(count) { output.append("\n") }
  }

  private fun appendVariableDeclaration(statement: Statement.Variable) {
    val variableKind = statement.designation
    val identifier = statement.identifier
    val dataType = statement.dataType
    val spaceForColons = ruleApplier.applySpaceForColon()
    val spacesAroundAssignment = ruleApplier.applySpacesAroundAssignment()
    output.append("$variableKind $identifier$spaceForColons$dataType$spacesAroundAssignment")
  }

  override fun visitVariableExpression(expression: Expression.Variable) {
    output.append(expression.name)
  }

  override fun visitAssign(expression: Expression.Assign) {
    appendAssignment(expression)
    expression.value.accept(this)
    output.append(";\n")
  }

  private fun appendAssignment(expression: Expression.Assign) {
    val spaceAroundAssignment = if (rules.spaceAroundAssignment) " " else ""
    output.append("${expression.name}$spaceAroundAssignment=$spaceAroundAssignment")
  }

  override fun visitBinary(expression: Expression.Binary) {
    expression.left.accept(this)
    appendBinaryOperator(expression)
    expression.right.accept(this)
  }

  private fun appendBinaryOperator(expression: Expression.Binary) {
    val spaceAroundOperator = if (rules.spaceAroundAssignment) " " else ""
    output.append("$spaceAroundOperator${expression.operator}$spaceAroundOperator")
  }

  override fun visitGrouping(expression: Expression.Grouping) {
    val spaceSeparator = if (rules.singleSpaceSeparation) " " else ""
    output.append("($spaceSeparator")
    expression.expression.accept(this)
    output.append("$spaceSeparator)")
  }

  override fun visitLiteral(expression: Expression.Literal) {
    output.append(if (expression.value is String) "\"${expression.value}\"" else expression.value)
  }

  override fun visitUnary(expression: Expression.Unary) {
    output.append(expression.operator)
    expression.right.accept(this)
  }

  override fun visitIdentifier(expression: Expression.IdentifierExpression) {
    output.append(expression.name)
  }

  private fun appendIndent() {
    repeat(currentIndent) { output.append(" ") }
  }
}
