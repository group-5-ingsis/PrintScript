package interpreter

import Environment
import lexer.Lexer
import parser.Parser

class PrintScript {
    fun run(input: String) : Environment {
        val lexer = Lexer(input)
        val parser = Parser(lexer)
        var scope = Environment()
        while (parser.hasNext()) {
            val ast = parser.next()
            scope = Interpreter.interpret(ast, scope)
        }
        return scope
    }

}