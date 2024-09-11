import interpreter.Interpreter
import lexer.Lexer
import org.junit.Assert.*
import org.junit.Test
import parser.Parser

class InterpreterV1_1 {


    val version = "1.1"

    @Test
    fun testDeclarationWithNumber() {
        val input = "let a: Boolean = true; if (a) { let queso: Number = 5; println(queso); } else {println(\"hola\");}"
        val version = "1.1"
        val tokens = Lexer(input, version)
        val parser = Parser(tokens, version)

        // Ejecutar la interpretación
        val (stringBuilder, env) = Interpreter.interpret(parser, version)

        // Verificar el contenido del StringBuilder
        val expectedOutput = "\n5\n" // Asumiendo que el valor de 'queso' se imprime seguido de un salto de línea
        assertEquals(expectedOutput, stringBuilder.toString())
        assertFalse(stringBuilder.toString().contains("hola"))
        assertTrue(env.contains("a"))
        assertFalse(env.contains("queso"))
    }


    @Test
    fun testDeclarationReAssignation() {
        val input = "let a: Boolean = true; if (a) {a = false; let arbol : Number = 44 ;  println(a); } else {println(\"hola\");}"
        val version = "1.1"
        val tokens = Lexer(input, version)
        val parser = Parser(tokens, version)

        // Ejecutar la interpretación
        val (stringBuilder, env) = Interpreter.interpret(parser, version)

        // Verificar el contenido del StringBuilder
        val expectedOutput = "\nfalse\n" // Asumiendo que el valor de 'queso' se imprime seguido de un salto de línea
        assertEquals(expectedOutput, stringBuilder.toString())

        assertTrue(env.contains("a"))
        assertEquals(env.get("a"), false)
        assertFalse(env.contains("arbol"))
    }

}