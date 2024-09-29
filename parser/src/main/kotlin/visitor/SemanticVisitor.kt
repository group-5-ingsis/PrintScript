package visitor

import environment.Environment
import nodes.Expression
import nodes.Statement
import utils.InputProvider

class SemanticVisitor(environment: Environment, readInput: InputProvider) : Visitor<SemanticVisitorResult> {
    override fun visitExpression(statement: Statement.StatementExpression): SemanticVisitorResult {
        TODO("Not yet implemented")
    }

    override fun visitVariableStatement(statement: Statement.Variable): SemanticVisitorResult {
        TODO("Not yet implemented")
    }

    override fun visitVariableExpression(expression: Expression.Variable): SemanticVisitorResult {
        TODO("Not yet implemented")
    }

    override fun visitBlock(statement: Statement.BlockStatement): SemanticVisitorResult {
        TODO("Not yet implemented")
    }

    override fun visitIf(statement: Statement.IfStatement): SemanticVisitorResult {
        TODO("Not yet implemented")
    }

    override fun visitAssign(expression: Expression.Assign): SemanticVisitorResult {
        TODO("Not yet implemented")
    }

    override fun visitBinary(expression: Expression.Binary): SemanticVisitorResult {
        TODO("Not yet implemented")
    }

    override fun visitGrouping(expression: Expression.Grouping): SemanticVisitorResult {
        TODO("Not yet implemented")
    }

    override fun visitLiteral(expression: Expression.Literal): SemanticVisitorResult {
        TODO("Not yet implemented")
    }

    override fun visitUnary(expression: Expression.Unary): SemanticVisitorResult {
        TODO("Not yet implemented")
    }

    override fun visitIdentifier(expression: Expression.IdentifierExpression): SemanticVisitorResult {
        TODO("Not yet implemented")
    }

    override fun visitPrint(statement: Statement.Print): SemanticVisitorResult {
        TODO("Not yet implemented")
    }

    override fun visitReadInput(expression: Expression.ReadInput): SemanticVisitorResult {
        TODO("Not yet implemented")
    }

    override fun visitReadEnv(expression: Expression.ReadEnv): SemanticVisitorResult {
        TODO("Not yet implemented")
    }
}
