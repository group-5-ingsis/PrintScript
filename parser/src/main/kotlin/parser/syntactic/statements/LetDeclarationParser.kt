package parser.syntactic.statements
import nodes.Expression
import nodes.Statement
import parser.syntactic.TokenManager
import parser.syntactic.expressions.ExpressionType
import token.Token

class LetDeclarationParser(private val expressionEvaluator: ExpressionType) : StatementParser {

    override fun parse(tokens: List<Token>): ParseStatementResult {
        var manager = TokenManager(tokens)
        val position = manager.getPosition()
        // a : Number =
        var initializer: Expression? = null
        val identifier = manager.consumeTokenType("IDENTIFIER")
        manager.consumeTokenValue(":")
        val dataType = manager.consumeTokenType("VARIABLE_TYPE").value

        if (manager.isValue("=")) {
            manager.consumeTokenValue("=")
            val (remainingTokens, exp) = expressionEvaluator.parse(manager.getTokens())
            initializer = exp
            manager = TokenManager(remainingTokens)
        }

        manager.consumeTokenValue(";")

        return Pair(manager.getTokens(), Statement.Variable("let", identifier.value, initializer, dataType, position))
    }
}
