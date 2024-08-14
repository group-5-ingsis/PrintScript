package parser

import lexer.Lexer
import org.junit.jupiter.api.Test
import token.Token

class ParserTester {

  private fun getTokenSublist(tokens: List<Token>): List<List<Token>> {
    val tokenSublists = mutableListOf<List<Token>>()
    var j = 0
    for ((index, token) in tokens.withIndex()) {
      if (token.type == "PUNCTUATION" && token.value == ";") {
        tokenSublists.add(tokens.subList(j, index))
        j += index + 1
      }
    }
    return tokenSublists
  }

  @Test
  fun testTokenSplittingBySemicolon() {
    val lexer = Lexer
    val tokens: List<Token> = lexer.lex("println(23);", listOf())
    println(getTokenSublist(tokens))
  }

  @Test
  fun testBuildDeclarationAST() {
    val syntaxParser = SyntacticParser()

    val tokens: List<Token> = Lexer.lex("let a: Number;", listOf())

    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)

    val children = ast.getChildren()
    println(children)
    for (node in children) {
      
      val nodeResult = node.solve()
      println(nodeResult)
    }
  }

  @Test
  fun testBuildAssignationASTWithNumber() {
    val lexer = Lexer
    val syntaxParser = SyntacticParser()
    val tokens: List<Token> = lexer.lex("x = 4;", listOf())
    val result: SyntacticParser.RootNode = syntaxParser.run(tokens)
    println(result.getChildren())
    for (node in result.getChildren()) {
      println(node.solve())
    }
  }

  @Test
  fun testBuildAssignationASTWithString() {
    val lexer = Lexer
    val syntaxParser = SyntacticParser()
    val tokens: List<Token> = lexer.lex("x = 'test';", listOf())
    val result: SyntacticParser.RootNode = syntaxParser.run(tokens)
    println(result.getChildren())
    for (node in result.getChildren()) {
      println(node.solve())
    }
  }

  @Test
  fun testBuildAssignationASTWithVariable() {
    val lexer = Lexer
    val syntaxParser = SyntacticParser()
    val tokens: List<Token> = lexer.lex("x = y;", listOf())
    val result: SyntacticParser.RootNode = syntaxParser.run(tokens)
    println(result.getChildren())
    for (node in result.getChildren()) {
      println(node.solve())
    }
  }

  @Test
  fun testBuildAssignDeclareAST() {
    val lexer = Lexer
    val syntaxParser = SyntacticParser()
    val tokens: List<Token> = lexer.lex("let a: Number = 7;", listOf())
    val result: SyntacticParser.RootNode = syntaxParser.run(tokens)
    println(result.getChildren())
    for (node in result.getChildren()) {
      println(node.solve())
    }
  }

  @Test
  fun testBuildMethodCallAST() {
    val lexer = Lexer
    val syntaxParser = SyntacticParser()
    val tokens: List<Token> = lexer.lex("println(3);", listOf())
  }
}