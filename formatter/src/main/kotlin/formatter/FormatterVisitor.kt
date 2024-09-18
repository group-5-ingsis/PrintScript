package formatter

import nodes.Expression
import nodes.StatementType
import position.visitor.Visitor
import rules.FormattingRules
import rules.RuleApplier

class FormatterVisitor(
    private val rules: FormattingRules,
    private val version: String
) : Visitor {

    private val output = StringBuilder()
    private val ruleApplier = RuleApplier(rules)
    private var currentIndent = 0

    fun getFormattedOutput(): String {
        return output.toString()
    }

    override fun visitPrintStm(statement: StatementType.Print) {
        appendPrintKeyword()
        val value = statement.value
        value.accept(this)
        output.append(";\n")
        appendNewlines(rules.newlineAfterPrintln)
    }

    override fun visitExpressionStm(statement: StatementType.StatementExpression) {
        val value = statement.value
        value.accept(this)
    }

    override fun visitVariableStm(statement: StatementType.Variable) {
        appendVariableDeclaration(statement)
        statement.initializer?.accept(this)
        output.append(";\n")
    }

    override fun visitBlockStm(statement: StatementType.BlockStatement) {
        if (version >= "1.1") {
            statement.listStm.forEach {
                appendIndent()
                it.accept(this)
            }
            currentIndent -= rules.blockIndentation
            appendIndent()
        }
    }

    override fun visitIfStm(statement: StatementType.IfStatement) {
        if (version >= "1.1") {
            appendIndent()
            output.append("if (")
            statement.condition.accept(this)
            output.append(")")
            handleBracesPlacement()
            processBranch(statement.thenBranch)
            statement.elseBranch?.let {
                output.append(" else ")
                processBranch(it)
            }
        }
    }

    private fun appendPrintKeyword() {
        val spaceSeparator = if (rules.singleSpaceSeparation) " " else ""
        output.append("println$spaceSeparator")
    }

    private fun appendNewlines(count: Int) {
        repeat(count) { output.append("\n") }
    }

    private fun appendVariableDeclaration(statement: StatementType.Variable) {
        val variableKind = statement.designation
        val identifier = statement.identifier
        val dataType = statement.dataType
        val spaceForColons = ruleApplier.applySpaceForColon()
        val spacesAroundAssignment = ruleApplier.applySpacesAroundAssignment()
        output.append("$variableKind $identifier$spaceForColons$dataType$spacesAroundAssignment")
    }

    private fun handleBracesPlacement() {
        if (rules.ifBraceSameLine) {
            output.append(" {\n")
        } else {
            output.append("\n")
            appendIndent()
            output.append("{\n")
        }
        currentIndent += rules.blockIndentation
    }

    private fun processBranch(branch: StatementType) {
        output.append("{\n")
        currentIndent += rules.blockIndentation
        appendIndent()

        branch.accept(this)

        currentIndent -= rules.blockIndentation
        appendIndent()
        output.append("}")
    }

    override fun visitVariable(expression: Expression.Variable) {
        output.append(expression.name)
    }

    override fun visitAssign(expression: Expression.Assign) {
        appendAssignment(expression)
        expression.value.accept(this)
        output.append(";")
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
        val value = expression.value
        output.append(if (value is String) "\"$value\"" else value)
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
