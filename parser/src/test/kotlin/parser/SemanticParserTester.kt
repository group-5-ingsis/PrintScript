package parser

import lexer.Lexer
import org.junit.jupiter.api.Test
import token.Token

class SemanticParserTester {
  @Test
  fun testAssignDeclareStatementShouldPass() {
    val lexer = Lexer
    val syntaxParser = SyntacticParser()
    val semanticParser = SemanticParser()
    val tokens: List<Token> = lexer.lex("let a: Number = \"testing\";", listOf())
    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
    val result = semanticParser.run(ast)
  }

  @Test
  fun testAssignDeclareStatementShouldFail() {
  }
}
