package formatter

import nodes.Expression
import nodes.StatementType
import position.visitor.Visitor
import rules.FormattingRules
import rules.RuleApplier

class FormatterVisitor(private val rules: FormattingRules) : Visitor {

    private val output = StringBuilder()
    private val ruleApplier = RuleApplier(rules)
    private var currentIndent = 0

    fun getFormattedOutput(): String {
        return output.toString()
    }

    override fun visitPrintStm(statement: StatementType.Print) {
        val value = statement.value

        val singleSpaceSeparation = rules.singleSpaceSeparation
        if (singleSpaceSeparation) {
            output.append("println ")
        } else {
            output.append("println")
        }

        value.accept(this)
        output.append(";\n")

        repeat(rules.newlineAfterPrintln) {
            output.append("\n")
        }
    }

    override fun visitExpressionStm(statement: StatementType.StatementExpression) {
        statement.value.accept(this)
    }

    override fun visitVariableStm(statement: StatementType.Variable) {
        val variableKind = statement.designation
        val identifier = statement.identifier
        val dataType = statement.dataType

        val spaceForColons = ruleApplier.applySpaceForColon()
        val spacesAroundAssignment = ruleApplier.applySpacesAroundAssignment()

        output.append("$variableKind $identifier")
        output.append("$spaceForColons$dataType")
        output.append(spacesAroundAssignment)

        statement.initializer?.accept(this)

        output.append(";\n")
    }

    private fun appendIndent() {
        repeat(currentIndent) {
            output.append(" ")
        }
    }

    override fun visitBlockStm(statement: StatementType.BlockStatement) {
        statement.listStm.forEach {
            appendIndent()
            it.accept(this)
        }
        currentIndent -= rules.blockIndentation
        appendIndent()
    }

    override fun visitIfStm(statement: StatementType.IfStatement) {
        appendIndent()
        output.append("if (")
        val condition = statement.condition
        condition.accept(this)
        output.append(")")

        val sameBraceLine = rules.ifBraceSameLine
        if (sameBraceLine) {
            output.append(" {\n")
        } else {
            output.append("\n")
            appendIndent()
            output.append("{\n")
        }

        currentIndent += rules.blockIndentation
        val thenBranch = statement.thenBranch
        thenBranch.accept(this)
        currentIndent -= rules.blockIndentation

        appendIndent()
        output.append("}")

        val elseBranch = statement.elseBranch
        elseBranch?.let {
            output.append(" else ")

            if (it is StatementType.BlockStatement) {
                output.append("{\n")
                currentIndent += rules.blockIndentation
                appendIndent()
                currentIndent += rules.blockIndentation
                it.accept(this)
                currentIndent -= rules.blockIndentation
                appendIndent()
                output.append("}")
            } else {
                output.append("{\n")
                currentIndent += rules.blockIndentation
                it.accept(this)
                currentIndent -= rules.blockIndentation
                appendIndent()
                output.append("}")
            }
        }
    }

    override fun visitVariable(expression: Expression.Variable) {
        val name = expression.name
        output.append(name)
    }

    override fun visitAssign(expression: Expression.Assign) {
        val spaceAroundAssignment = if (rules.spaceAroundAssignment) " " else ""
        output.append("${expression.name}$spaceAroundAssignment=$spaceAroundAssignment")
        expression.value.accept(this)
        output.append(";")
    }

    override fun visitBinary(expression: Expression.Binary) {
        expression.left.accept(this)
        val spaceAroundOperator = if (rules.spaceAroundAssignment) " " else ""
        output.append("$spaceAroundOperator${expression.operator}$spaceAroundOperator")
        expression.right.accept(this)
    }

    override fun visitGrouping(expression: Expression.Grouping) {
        val singleSpaceSeparation = rules.singleSpaceSeparation
        if (singleSpaceSeparation) {
            output.append("( ")
        } else {
            output.append("(")
        }

        val insideExpression = expression.expression
        insideExpression.accept(this)

        if (singleSpaceSeparation) {
            output.append(" )")
        } else {
            output.append(")")
        }
    }

    override fun visitLiteral(expression: Expression.Literal) {
        val value = expression.value

        if (value is String) {
            output.append("\"$value\"")
            return
        }

        output.append(value)
    }

    override fun visitUnary(expression: Expression.Unary) {
        output.append(expression.operator)
        expression.right.accept(this)
    }

    override fun visitIdentifier(expression: Expression.IdentifierExpression) {
        output.append(expression.name)
    }
}
