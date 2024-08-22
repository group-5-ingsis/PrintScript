package parser

import lexer.Lexer
import parser.exceptions.SemanticErrorException
import token.Token
import kotlin.test.Test
import kotlin.test.assertFailsWith

class SemanticParserTester {
  @Test
  fun testAssignDeclareStatementShouldPass() {
    val lexer = Lexer
    val syntaxParser = SyntacticParser()
    val semanticParser = SemanticParser()
    val tokens: List<Token> = lexer.lex("let a: Number = 23;", listOf())
    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
    // TODO fix AssignationValue.
    // semanticParser.run(ast)
  }

  @Test
  fun testAssignDeclareStatementShouldFail() {
    val lexer = Lexer
    val syntaxParser = SyntacticParser()
    val semanticParser = SemanticParser()
    val tokens: List<Token> = lexer.lex("let a: String = 23;", listOf())
    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
    assertFailsWith(SemanticErrorException::class) {
      semanticParser.run(ast)
    }
  }
}
