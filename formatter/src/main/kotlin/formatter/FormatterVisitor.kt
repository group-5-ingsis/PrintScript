package formatter

import nodes.Expression
import nodes.StatementType
import position.visitor.Visitor
import rules.FormattingRules
import rules.RuleApplier

class FormatterVisitor(private val rules: FormattingRules) : Visitor {

    private val output = StringBuilder()
    private val ruleApplier = RuleApplier(rules)

    fun getFormattedOutput(): String {
        return output.toString()
    }

    override fun visitPrintStm(statement: StatementType.Print) {
        repeat(rules.newlineBeforePrintln) {
            output.append("\n")
        }
        val value = statement.value
        output.append("println")
        value.accept(this)
        output.append(";\n")
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
        output.append("(")
        val insideExpression = expression.expression
        insideExpression.accept(this)
        output.append(")")
    }

    override fun visitLiteral(expression: Expression.Literal) {
        val value = expression.value
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
