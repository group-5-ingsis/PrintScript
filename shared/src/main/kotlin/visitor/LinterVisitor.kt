//package visitor
//
//import composite.Node
//import nodes.Expression
//import nodes.StatementType
//import position.Position
//
//class LinterVisitor(private val linterRulesMap: Map<String, Any>) : Visitor {
//
//    override fun visitAssignation(assignation: Node.Assignation) {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitDeclaration(declaration: Node.Declaration) {
//        val position: Position = declaration.identifierPosition
//        val row: Int = position.line
//        val col: Int = position.symbolIndex
//        if (linterRulesMap.containsKey("identifier_naming_convention")) {
//            val identifierNamingConvention = linterRulesMap["identifier_naming_convention"]
//            when (identifierNamingConvention) {
//                "\"snake-case\"" -> {
//                    if (!Regex("^[a-z]+(_[a-z]+)*\$\n").matches(declaration.identifier)) {
//                        throw Exception("Identifier ${declaration.identifier} at row $row column $col does not follow snake_case naming convention")
//                    }
//                    return
//                }
//                "\"camel-case\"" -> {
//                    if (!Regex("[a-z]+([A-Z0-9][a-z0-9]*)*").matches(declaration.identifier)) {
//                        throw Exception("Identifier ${declaration.identifier} at row $row column $col does not follow camelCase naming convention")
//                    }
//                    return
//                }
//                "\"off\"" -> {
//                    return
//                }
//                else -> throw Exception("Unsupported identifier naming convention: $identifierNamingConvention")
//            }
//        }
//        // Do nothing
//    }
//
//    override fun visitAssignDeclare(assignationDeclaration: Node.AssignationDeclaration) {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitMethodCall(methodCall: Node.Method) {
//        val position: Position = methodCall.identifierPosition
//        val row: Int = position.line
//        val col: Int = position.symbolIndex
//        if (methodCall.identifier.value == "println" && linterRulesMap.containsKey("println-expression-allowed")) {
//            when (linterRulesMap["println-expression-allowed"]) {
//                "true" -> {
//                    return
//                }
//                "false" -> {
//                    val arguments: List<Node> = methodCall.arguments.argumentsOfAnyTypes
//                    if (arguments.size != 1) {
//                        throw Exception("println at row $row column $col cannot have an expression as an argument, it must have either a literal or an identifier")
//                    }
//                    return
//                }
//                else -> throw Exception("Unsupported println expression allowed value: ${linterRulesMap["println-expression-allowed"]}")
//            }
//        }
//    }
//
//    override fun visitLiteralExp(exp: Expression.Literal): Any? {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitGroupExp(exp: Expression.Grouping): Any? {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitUnaryExpr(exp: Expression.Unary): Any? {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitBinaryExpr(exp: Expression.Binary): Any? {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitVariableExp(exp: Expression.Variable): Any? {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitAssignExpr(exp: Expression.Assign): Any? {
//        TODO("Not yet implemented")
//    }
//
//    override fun getVisitorFunctionForExpression(expressionType: String): (Expression) -> Any? {
//        TODO("Not yet implemented")
//    }
//
//    override fun getVisitorFunctionForStatement(statementType: String): (StatementType) -> Unit {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitPrintStm(statement: StatementType.Print) {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitExpressionStm(statement: StatementType.StatementExpression) {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitVariableStm(statement: StatementType.Variable) {
//        if (isAssignDeclaration(statement))
//    }
//
//    private fun isAssignDeclaration(statement: StatementType.Variable): Boolean{
//
//    }
//
//    override fun visitIdentifier(exp: Expression.IdentifierExpression): Any? {
//        TODO("Not yet implemented")
//    }
//}
