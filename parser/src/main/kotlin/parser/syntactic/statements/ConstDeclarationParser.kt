package parser.syntactic.statements

import ExpressionType
import exception.SemanticErrorException
import nodes.Expression
import nodes.StatementType
import nodes.Type
import parser.syntactic.TokenManager
import token.Token

class ConstDeclarationParser(private val expressionEvaluator: ExpressionType) : StatementParser {

  override fun parse(tokens: List<Token>): ParseStatementResult {
    var manager = TokenManager(tokens)
    val position = manager.getPosition()

    val identifier = manager.consumeTokenType("IDENTIFIER")
    var initializer: Expression? = null

    manager.consumeTokenValue(":")
    val dataType = manager.consumeTokenType("VARIABLE_TYPE").value

    if (manager.isValue(";")) {
      throw SemanticErrorException("Invalid procedure: variable '${identifier.value}' of type 'const' cannot be declared. ")
    }

    manager.consumeTokenValue("=")
    val getType = Type.stringToType(dataType)
    val (remainingTokens, exp) = expressionEvaluator.parse(manager.getTokens(), getType)
    initializer = exp
    manager = TokenManager(remainingTokens)

    manager.consumeTokenValue(";")

    return Pair(manager.getTokens(), StatementType.Variable("const", identifier.value, initializer, dataType, position))
  }
}
