package visitor


import nodes.Expression
import nodes.StatementType

interface Visitor {
  fun visitLiteralExp(exp: Expression.Literal): Any?

  fun visitGroupExp(exp: Expression.Grouping): Any?

  fun visitUnaryExpr(exp: Expression.Unary): Any?

  fun visitBinaryExpr(exp: Expression.Binary): Any?

  fun getVisitorFunctionForExpression(expressionType: String): (Expression) -> Any?

  fun getVisitorFunctionForStatement(statementType: String): (StatementType) -> Unit

  fun visitPrintStm(statement: StatementType.Print)
  fun visitExpressionStm(statement: StatementType.StatementExpression)
  fun visitVariableStm(statement: StatementType.Variable)

  fun visitIdentifier(exp: Expression.IdentifierExpression): Any?




}
