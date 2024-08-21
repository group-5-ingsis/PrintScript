package parser

import lexer.Lexer
import token.Token
import kotlin.test.Test

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
