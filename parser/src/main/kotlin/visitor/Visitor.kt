package position.visitor

interface Visitor {
    fun visitPrintStm(statement: nodes.StatementType.Print)
    fun visitExpressionStm(statement: nodes.StatementType.StatementExpression)
    fun visitVariableStm(statement: nodes.StatementType.Variable)
    fun visitBlockStm(statement: nodes.StatementType.BlockStatement)
    fun visitIfStm(statement: nodes.StatementType.IfStatement)
    fun visitVariable(expression: nodes.Expression.Variable)
    fun visitAssign(expression: nodes.Expression.Assign)
    fun visitBinary(expression: nodes.Expression.Binary)
    fun visitGrouping(expression: nodes.Expression.Grouping)
    fun visitLiteral(expression: nodes.Expression.Literal)
    fun visitUnary(expression: nodes.Expression.Unary)
    fun visitIdentifier(expression: nodes.Expression.IdentifierExpression)
    fun visitReadInput(readInput: nodes.Expression.ReadInput) {}
    fun visitReadEnv(readEnv: nodes.Expression.ReadEnv) {}
}
