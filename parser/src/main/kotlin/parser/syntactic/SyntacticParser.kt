package parser.syntactic

import Environment
import nodes.StatementType
import parser.syntactic.statements.GenericStatementParser
import position.visitor.StatementVisitor
import position.visitor.Visitor
import position.visitor.statementVisitorResult
import token.Token

object SyntacticParser {

    fun parse(tokens: List<Token>, version: String): Pair<StatementType, List<Token>> {
        val currentParser = GenericStatementParser.makeStatementParser(version)

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

        fun accept(visitor: StatementVisitor, environment: Environment, sb: StringBuilder): statementVisitorResult {
            var env = environment.getCopy()
            var sbb = StringBuilder(sb.toString())
            for (child in children) {
                val result = child.acceptVisitor(visitor, env, sb)
                env = result.second
                sbb = result.first
            }
            return Pair(sbb, env)
        }

        fun accept(visitor: Visitor) {
            for (child in children) {
                child.accept(visitor)
            }
        }
    }
}
