package parser.syntactic

import Environment
import nodes.StatementType
import parser.syntactic.statements.GenericStatementParser
import position.visitor.StatementVisitor
import position.visitor.Visitor
import token.Token

object SyntacticParser {
    /* Notes on types of Expression/Statement on 'syntactic-parser-notes.txt' */

    fun parse(tokens: List<Token>): Pair<StatementType, List<Token>> {
        val currentParser = GenericStatementParser.makeStatementParser()
        val (remainingTokens, exp) = currentParser.parse(tokens)
        return Pair(exp, remainingTokens)
    }

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
