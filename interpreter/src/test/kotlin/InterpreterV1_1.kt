import interpreter.Interpreter
import lexer.Lexer
import org.junit.Assert.*
import org.junit.Test
import parser.Parser
import position.visitor.Environment

class InterpreterV1_1 {

    val version = "1.1"

    @Test
    fun testDeclarationWithNumber() {
        val input = "let a: boolean = true; if(a){ let queso: number = 5; println(queso); } else {println(\"hola\");}"
        val version = "1.1"
        val tokens = Lexer(input, version)
        val parser = Parser(tokens, version)

        val ast1 = parser.next()
        val ifstm = parser.next()


        // Ejecutar la interpretación
        val (_, env) = Interpreter.interpret(ast1, version, Environment())
        val (stringBuilder, env2) = Interpreter.interpret(ifstm, version, env)


        val expectedOutput = "5" // Asumiendo que el valor de 'queso' se imprime seguido de un salto de línea
        assertEquals(expectedOutput, stringBuilder.toString())
        assertFalse(stringBuilder.toString().contains("hola"))
        assertTrue(env2.contains("a"))
        assertFalse(env2.contains("queso"))


    }

    @Test
    fun testDeclarationReAssignation() {
        val input = "let a: boolean = true; if (a) {a = false; let arbol : number = 44 ;  println(a); } else {println(\"hola\");}"
        val version = "1.1"
        val tokens = Lexer(input, version)
        val parser = Parser(tokens, version)

        val ast1 = parser.next()
        val ifstm = parser.next()


        // Ejecutar la interpretación
        val (_, env) = Interpreter.interpret(ast1, version, Environment())
        val (stringBuilder, env2) = Interpreter.interpret(ifstm, version, env)




        // Verificar el contenido del StringBuilder
        val expectedOutput = "\nfalse\n" // Asumiendo que el valor de 'queso' se imprime seguido de un salto de línea
        assertEquals(expectedOutput, stringBuilder.toString())
        assertFalse(expectedOutput.contains("hola"))
        assertTrue(env2.contains("a"))
        assertEquals(env2.get("a"), false)
        assertFalse(env2.contains("arbol"))
    }
}