package parser.syntactic

import Environment
import nodes.StatementType
import parser.syntactic.statements.GenericStatement
import position.visitor.StatementVisitor
import position.visitor.Visitor
import token.Token

object SyntacticParser {

    fun parse(tokens: List<Token>): Pair<StatementType, List<Token>> {
        if (tokens.isNotEmpty()) {
            val currentParser = GenericStatement.makeStatementParser()
            val (remainingTokens, exp) = currentParser.parse(tokens)
            return Pair(exp, remainingTokens)
        }
        throw Exception("No tokens to parse")
    }

    /**
     * DIFFERENT KIND OF EXPRESSIONS
     *
     *   expression     → assigment ;
     *   assigment      → IDENTIFIER "=" assignment
     *                | comparison ;
     *   comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
     *   term           → factor ( ( "-" | "+" ) factor )* ;
     *   factor         → unary ( ( "/" | "*" ) unary )* ;
     *   unary          → ( "!" | "-" ) unary
     *   | primary ;
     *   primary        → NUMBER | STRING | "true" | "false" | "null"
     *   | "(" expression ")" | IDENTIFIER ;
     *
     */

    /**
     * DIFFERENT KIND OF STATEMENTS:
     *
     * program        →  declaration* EOF ;
     *
     * declaration    → varDecl
     *                | statement ; // later here: functions, and classes
     *
     * statement      → exprStmt
     *                | printStmt ;
     *
     * exprStmt       → expression ";" ;
     * printStmt      → "print" expression ";" ;
     * varDecl        → "let" IDENTIFIER VARIABLE_TYPE ( "=" expression )? ";" ;
     *
     */

    class RootNode private constructor() {
        private val children = mutableListOf<StatementType>()

        fun addChild(child: StatementType) {
            children.add(child)
        }

        fun getChildren(): List<StatementType> {
            return children
        }

        companion object {
            internal fun create(): RootNode {
                return RootNode()
            }
        }

        fun accept(visitor: StatementVisitor, environment: Environment): Environment {
            var env = environment.getCopy()
            for (child in children) {
                env = child.acceptVisitor(visitor, env)
            }
            return env
        }

        fun accept(visitor: Visitor) {
            for (child in children) {
                child.accept(visitor)
            }
        }
    }
}
