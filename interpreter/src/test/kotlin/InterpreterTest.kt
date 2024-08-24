import interpreter.Interpreter
import lexer.Lexer
import parser.SyntacticParser
import token.Token
import visitor.VariableTable
import kotlin.test.Test
import kotlin.test.assertEquals

class InterpreterTest {
  private val syntaxParser = SyntacticParser()

  @Test
  fun testMethodCallWithString() {
    val tokens: List<Token> = Lexer.lex("let a: String; a = \"Hello\"; println(a);", listOf())
    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
    val output = Interpreter.interpret(ast)
    assertEquals("\"Hello\"", output)
  }

  @Test
  fun testMethodCallWithNumber() {
    val tokens: List<Token> = Lexer.lex("let a: Number = 4; println(a);", listOf())
    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
    val output = Interpreter.interpret(ast)
    assertEquals("4", output)
  }

  @Test
  fun testDeclarationWithNumber() {
    val tokens: List<Token> = Lexer.lex("let a: Number;", listOf())
    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
    Interpreter.interpret(ast)
    assertEquals("undefined", VariableTable.getVariable("a"))
  }

  @Test
  fun testDeclarationWithString() {
    val tokens: List<Token> = Lexer.lex("let a: String;", listOf())
    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
    Interpreter.interpret(ast)
    assertEquals("undefined", VariableTable.getVariable("a"))
  }

  @Test
  fun testAssignationWithString() {
    val tokens: List<Token> = Lexer.lex("a = \"Hello World\";", listOf())
    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
    Interpreter.interpret(ast)
    assertEquals("\"Hello World\"", VariableTable.getVariable("a"))
  }

  @Test
  fun testAssignationWithNumber() {
    val tokens: List<Token> = Lexer.lex("a = 2;", listOf())
    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
    Interpreter.interpret(ast)
    assertEquals(2, VariableTable.getVariable("a"))
  }

  @Test
  fun testAssignationWithLiteral() {
    val tokens: List<Token> = Lexer.lex("let b: Number = 3; let a: Number; a = b;", listOf())
    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
    Interpreter.interpret(ast)
    assertEquals(3, VariableTable.getVariable("a"))
  }

  @Test
  fun testAssignationDeclarationWithNumber() {
    val tokens: List<Token> = Lexer.lex("let a: Number = 2; a = 3;", listOf())
    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
    Interpreter.interpret(ast)
    assertEquals(3, VariableTable.getVariable("a"))
  }

  @Test
  fun testAssignationDeclarationWithString() {
    val tokens: List<Token> = Lexer.lex("let b: String; b = \"Hello\";", listOf())
    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
    Interpreter.interpret(ast)
    assertEquals("\"Hello\"", VariableTable.getVariable("b"))
  }

  @Test
  fun testSumNumber() {
    val tokens: List<Token> = Lexer.lex("let a: Number = 6 + 2;", listOf())
    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
    Interpreter.interpret(ast)
    assertEquals(8.0, VariableTable.getVariable("a"))
  }

  @Test
  fun testDivisionNumber() {
    val tokens: List<Token> = Lexer.lex("let a: Number = 6 / 2;", listOf())
    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
    Interpreter.interpret(ast)
    assertEquals(3.0, VariableTable.getVariable("a"))
  }

  @Test
  fun testSumWithIdentifier() {
    val tokens: List<Token> = Lexer.lex("let a: Number = 6; let b: Number = a + 2;", listOf())
    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
    Interpreter.interpret(ast)
    assertEquals(8.0, VariableTable.getVariable("b"))
  }

//  @Test
//  fun testBinaryOperationString() {
//    val tokens: List<Token> = Lexer.lex("let a: String = \"Hello\" + \"World\";", listOf())
//    val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
//    interpreter.interpret(ast)
//    assertEquals("\"HelloWorld\"", VariableTable.getVariable("a"))
//  }
}
