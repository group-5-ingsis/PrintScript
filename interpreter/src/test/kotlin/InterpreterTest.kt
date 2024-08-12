import interpreter.Interpreter
import lexer.Lexer
import parser.SyntacticParser
import token.Token
import kotlin.test.Test

class InterpreterTest {

    @Test
    fun testDeclarationInterpreter() {
        val syntaxParser = SyntacticParser()

        val tokens: List<Token> = Lexer.lex("let a: Number;", listOf())

        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)

        val interpretation = Interpreter.interpret(ast)

        println(interpretation)
    }
}