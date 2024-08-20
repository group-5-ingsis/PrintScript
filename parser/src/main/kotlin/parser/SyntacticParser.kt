package parser

import Node
import parser.builders.*
import parser.statement.Statement
import parser.statement.StatementManager
import token.Token
import visitor.NodeVisitor

class SyntacticParser {
    private val builders: Map<String, ASTBuilder> =
        mapOf(
            "Declaration" to DeclarationASTBuilder(),
            "Assignation" to AssignationASTBuilder(),
            "AssignDeclare" to AssignDeclareASTBuilder(),
            "MethodCall" to MethodCallASTBuilder(),
        )

    fun run(tokens: List<Token>): RootNode {
        return parse(tokens)
    }

    private fun parse(tokens: List<Token>): RootNode {
        var statements: List<Statement> = getStatements(tokens)
        statements = StatementManager.categorize(statements)
        return buildAST(statements)
    }

    private fun buildAST(categorizedStatements: List<Statement>): RootNode {
        val root = RootNode.create()
        for (statement in categorizedStatements) {
            val statementType = statement.statementType
            val builder = builders[statementType]
            if (builder != null) {
                val child = builder.build(statement)
                root.addChild(child)
            } else {
                throw UnsupportedOperationException("Unexpected statement")
            }
        }
        return root
    }

    private fun getStatements(tokens: List<Token>): List<Statement> {
        val statements = mutableListOf<Statement>()
        var j = 0

        for ((index, token) in tokens.withIndex()) {
            if (token.type == "PUNCTUATION" && token.value == ";") {
                val sublist = tokens.subList(j, index + 1)
                val statementType = "UNKNOWN"
                statements.add(Statement(sublist, statementType))
                j = index + 1
            }
        }

        return statements
    }

    class RootNode private constructor() {
        private val children = mutableListOf<Node>()

        fun addChild(child: Node) {
            children.add(child)
        }

        fun getChildren(): List<Node> {
            return children
        }

        companion object {
            internal fun create(): RootNode {
                return RootNode()
            }
        }

        fun accept(visitor: NodeVisitor) {
            for (child in children) {
                child.acceptVisitor(visitor)
            }
        }
    }
}
