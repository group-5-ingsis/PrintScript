package formatter

import nodes.Expression
import nodes.Statement
import rules.FormattingRules
import rules.RuleApplier
import visitor.Visitor

class FormatterVisitor(
    private val rules: FormattingRules,
    private val version: String
) : Visitor<String> {

    private val output = StringBuilder()
    private val ruleApplier = RuleApplier(rules)
    private var currentIndent = 0

    fun getFormattedOutput(): String {
        return output.toString()
    }

    override fun visitPrint(statement: Statement.Print): String {
        appendPrintKeyword()
        statement.value.accept(this)
        output.append(";\n")
        appendNewlines(rules.newlineAfterPrintln)
        return output.toString()
    }

    override fun visitExpression(statement: Statement.StatementExpression): String {
        val expression = statement.value
        expression.accept(this)
        return output.toString()
    }

    override fun visitVariable(statement: Statement.Variable): String {
        appendVariableDeclaration(statement)
        appendVariableInitializer(statement.initializer)
        output.append(";\n")
        return output.toString()
    }

    private fun appendVariableInitializer(initializer: Expression?): String {
        initializer?.accept(this)
        return output.toString()
    }

    override fun visitBlockStm(statement: Statement.BlockStatement): String {
        if (version >= "1.1") {
            val statements = statement.listStm
            processBlockStatements(statements)
            finalizeBlockIndentation()
        }
        return output.toString()
    }

    private fun processBlockStatements(statements: List<Statement>): String {
        statements.forEach {
            appendIndent()
            it.accept(this)
        }
        return output.toString()
    }

    private fun finalizeBlockIndentation(): String {
        currentIndent -= rules.blockIndentation
        appendIndent()
        return output.toString()
    }

    override fun visitIf(statement: Statement.IfStatement): String {
        handleIfCondition(statement.condition)
        handleThenBranch(statement.thenBranch)
        handleElseBranch(statement.elseBranch)
        return output.toString()
    }

    private fun handleIfCondition(condition: Expression): String {
        output.append("if (")
        condition.accept(this)
        output.append(")")
        appendBracesForCondition()
        return output.toString()
    }

    private fun appendBracesForCondition(): String {
        if (rules.ifBraceSameLine) {
            output.append(" {\n")
        } else {
            output.append("\n")
            appendIndent()
            output.append("{\n")
        }
        currentIndent += rules.blockIndentation
        return output.toString()
    }

    private fun handleThenBranch(thenBranch: Statement): String {
        thenBranch.accept(this)
        currentIndent -= rules.blockIndentation
        appendIndent()
        output.append("}")
        return output.toString()
    }

    private fun handleElseBranch(elseBranch: Statement?): String {
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
        return output.toString()
    }

    private fun appendPrintKeyword(): String {
        val spaceSeparator = if (rules.singleSpaceSeparation) " " else ""
        output.append("println$spaceSeparator")
        return output.toString()
    }

    private fun appendNewlines(count: Int): String {
        repeat(count) { output.append("\n") }
        return output.toString()
    }

    private fun appendVariableDeclaration(statement: Statement.Variable): String {
        val variableKind = statement.designation
        val identifier = statement.identifier
        val dataType = statement.dataType
        val spaceForColons = ruleApplier.applySpaceForColon()
        val spacesAroundAssignment = ruleApplier.applySpacesAroundAssignment()
        output.append("$variableKind $identifier$spaceForColons$dataType$spacesAroundAssignment")
        return output.toString()
    }

    override fun visitVariable(expression: Expression.Variable): String {
        output.append(expression.name)
        return output.toString()
    }

    override fun visitAssign(expression: Expression.Assign): String {
        appendAssignment(expression)
        expression.value.accept(this)
        output.append(";\n")
        return output.toString()
    }

    private fun appendAssignment(expression: Expression.Assign): String {
        val spaceAroundAssignment = if (rules.spaceAroundAssignment) " " else ""
        output.append("${expression.name}$spaceAroundAssignment=$spaceAroundAssignment")
        return output.toString()
    }

    override fun visitBinary(expression: Expression.Binary): String {
        expression.left.accept(this)
        appendBinaryOperator(expression)
        expression.right.accept(this)
        return output.toString()
    }

    private fun appendBinaryOperator(expression: Expression.Binary): String {
        val spaceAroundOperator = if (rules.spaceAroundAssignment) " " else ""
        output.append("$spaceAroundOperator${expression.operator}$spaceAroundOperator")
        return output.toString()
    }

    override fun visitGrouping(expression: Expression.Grouping): String {
        val spaceSeparator = if (rules.singleSpaceSeparation) " " else ""
        output.append("($spaceSeparator")
        expression.expression.accept(this)
        output.append("$spaceSeparator)")
        return output.toString()
    }

    override fun visitLiteral(expression: Expression.Literal): String {
        output.append(if (expression.value is String) "\"${expression.value}\"" else expression.value)
        return output.toString()
    }

    override fun visitUnary(expression: Expression.Unary): String {
        output.append(expression.operator)
        expression.right.accept(this)
        return output.toString()
    }

    override fun visitIdentifier(expression: Expression.IdentifierExpression): String {
        output.append(expression.name)
        return output.toString()
    }

    override fun visitReadInput(expression: Expression.ReadInput): String {
        TODO("Not yet implemented")
    }

    override fun visitReadEnv(expression: Expression.ReadEnv): String {
        TODO("Not yet implemented")
    }

    private fun appendIndent(): String {
        repeat(currentIndent) { output.append(" ") }
        return output.toString()
    }
}
