import interpreter.Interpreter
import lexer.Lexer
import parser.SyntacticParser
import token.Token
import kotlin.test.Test
import kotlin.test.assertEquals

class InterpreterTest {

    private val syntaxParser = SyntacticParser()

    @Test
    fun testDeclarationInterpreter() {

        val tokens: List<Token> = Lexer.lex("let a: Number;", listOf())

        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)

        Interpreter.interpret(ast)

        val variableTable = Interpreter.getVariableTable()

        assertEquals("Number", variableTable.getVariable("a"))
    }

    @Test
    fun testAssignation(){
        val tokens: List<Token> = Lexer.lex("a = 6;", listOf())
        val ast: SyntacticParser.RootNode = syntaxParser.run(tokens)
        Interpreter.interpret(ast)
        val variableTable = Interpreter.getVariableTable()
        assertEquals(6, variableTable.getVariable("a"))
    }
    
}