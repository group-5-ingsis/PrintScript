package parser.syntactic.statements

import nodes.StatementType
import parser.syntactic.TokenManager
import token.Token

class BlockStatementParser(val declarationParser: ()-> StatementParser): StatementParser {
    override fun parse(tokens: List<Token>): ParseStatementResult {


        val statementParser = declarationParser()
        val position = TokenManager(tokens).getPosition()
        val statements = mutableListOf<StatementType>()
        var manager = TokenManager(tokens)

        while (!manager.nextTokenMatchesExpectedType("RIGHT_BRACE") && manager.isNotTheEndOfTokens() ) {
            val (remainingTokens, statement) = statementParser.parse(manager.getTokens())
            manager = TokenManager(remainingTokens)
            statements.add(statement)
        }
        manager.consumeTokenType("RIGHT_BRACE")
        return Pair(manager.getTokens(), StatementType.BlockStatement(position, statements))


    }
}